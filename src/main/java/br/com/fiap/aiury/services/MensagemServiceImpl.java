package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.MensagemMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao da camada de servico para o recurso de mensagem.
 *
 * Responsabilidades:
 * - validar existencia de chat e remetente antes da persistencia;
 * - delegar conversoes para {@link MensagemMapper};
 * - encapsular operacoes transacionais de escrita.
 */
@Service
public class MensagemServiceImpl implements MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final AiuryAuthenticatedUserService authenticatedUserService;
    private final MensagemMapper mensagemMapper;

    @Autowired
    public MensagemServiceImpl(MensagemRepository mensagemRepository,
                               ChatRepository chatRepository,
                               UsuarioRepository usuarioRepository,
                               AjudanteRepository ajudanteRepository,
                               AiuryAuthenticatedUserService authenticatedUserService,
                               MensagemMapper mensagemMapper) {
        this.mensagemRepository = mensagemRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.mensagemMapper = mensagemMapper;
    }

    /**
     * Cria mensagem apos validar chat e remetente.
     */
    @Override
    @Transactional
    public Mensagem criarMensagem(MensagemRequestDTO mensagemDTO) {
        Chat chat = buscarChatPorId(mensagemDTO.getChatId());
        Usuario remetente = buscarUsuarioPorId(mensagemDTO.getRemetenteId());
        Ajudante remetenteAjudante = buscarAjudantePorId(mensagemDTO.getRemetenteAjudanteId());
        validarMensagem(chat, remetente, remetenteAjudante, mensagemDTO);

        Mensagem mensagem = mensagemMapper.toEntity(mensagemDTO, chat, remetente, remetenteAjudante);
        return mensagemRepository.save(mensagem);
    }

    /**
     * Busca mensagem por ID com tratamento de nao encontrado.
     */
    @Override
    public Mensagem buscarPorId(Long id) {
        Mensagem mensagem = mensagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mensagem nao encontrada com ID: " + id));
        validarAcessoAoChat(mensagem.getChat());
        return mensagem;
    }

    /**
     * Lista mensagens com filtros opcionais por chat/remetente.
     */
    @Override
    public List<Mensagem> buscarTodos(Long chatId, Long remetenteId) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        Specification<Mensagem> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (chatId != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("chat").get("id"), chatId)
            );
        }

        if (remetenteId != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> {
                        Join<Mensagem, Usuario> remetenteUsuario = root.join("remetente", JoinType.LEFT);
                        Join<Mensagem, Ajudante> remetenteAjudante = root.join("remetenteAjudante", JoinType.LEFT);
                        return criteriaBuilder.or(
                                criteriaBuilder.equal(remetenteUsuario.get("id"), remetenteId),
                                criteriaBuilder.equal(remetenteAjudante.get("id"), remetenteId)
                        );
                    }
            );
        }

        if (principal.isUsuario()) {
            Long usuarioId = principal.getUsuarioId();
            if (usuarioId == null) {
                throw new AccessDeniedException("Perfil de usuario autenticado sem vinculo valido.");
            }
            if (remetenteId != null && !remetenteId.equals(usuarioId)) {
                throw new AccessDeniedException("O usuario logado nao possui acesso ao remetente informado.");
            }
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("chat").get("usuario").get("id"), usuarioId)
            );
        } else if (principal.isAjudante()) {
            Long ajudanteId = principal.getAjudanteId();
            if (ajudanteId == null) {
                throw new AccessDeniedException("Perfil de ajudante autenticado sem vinculo valido.");
            }
            if (remetenteId != null && !remetenteId.equals(ajudanteId)) {
                throw new AccessDeniedException("O ajudante logado nao possui acesso ao remetente informado.");
            }
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("chat").get("ajudante").get("id"), ajudanteId)
            );
        }

        return mensagemRepository.findAll(specification, Sort.by(Sort.Direction.ASC, "dataEnvio"));
    }

    /**
     * Atualiza mensagem existente mantendo validacao de referencias.
     */
    @Override
    @Transactional
    public Mensagem atualizarMensagem(Long id, MensagemRequestDTO mensagemDTO) {
        exigirAdmin();
        Mensagem mensagemExistente = buscarPorId(id);

        Chat chat = buscarChatPorId(mensagemDTO.getChatId());
        Usuario remetente = buscarUsuarioPorId(mensagemDTO.getRemetenteId());
        Ajudante remetenteAjudante = buscarAjudantePorId(mensagemDTO.getRemetenteAjudanteId());
        validarMensagem(chat, remetente, remetenteAjudante, mensagemDTO);

        mensagemMapper.updateEntityFromDto(mensagemExistente, mensagemDTO, chat, remetente, remetenteAjudante);
        return mensagemRepository.save(mensagemExistente);
    }

    /**
     * Exclui mensagem por ID com validacao de existencia.
     */
    @Override
    @Transactional
    public void deletarMensagem(Long id) {
        exigirAdmin();
        if (!mensagemRepository.existsById(id)) {
            throw new NotFoundException("Mensagem nao encontrada com ID: " + id);
        }
        mensagemRepository.deleteById(id);
    }

    /**
     * Resolve chat por ID para uso interno na regra de negocio.
     */
    private Chat buscarChatPorId(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat nao encontrado com ID: " + id));
    }

    /**
     * Resolve usuario por ID para uso interno na regra de negocio.
     */
    private Usuario buscarUsuarioPorId(Long id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }

    private Ajudante buscarAjudantePorId(Long id) {
        if (id == null) {
            return null;
        }
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
    }

    private void validarMensagem(Chat chat,
                                 Usuario remetente,
                                 Ajudante remetenteAjudante,
                                 MensagemRequestDTO mensagemDTO) {
        validarAcessoAoChat(chat);
        validarRemetenteNoChat(chat, remetente, remetenteAjudante);
        validarRemetenteDoUsuarioLogado(remetente, remetenteAjudante);

        if (isFinalizado(chat.getStatus())) {
            throw new IllegalArgumentException("Nao e possivel enviar mensagens em chats finalizados.");
        }

        if (mensagemDTO.getDataEnvio().isBefore(chat.getDataInicio())) {
            throw new IllegalArgumentException("A data/hora da mensagem nao pode ser anterior ao inicio do chat.");
        }

        if (chat.getDataFim() != null && mensagemDTO.getDataEnvio().isAfter(chat.getDataFim())) {
            throw new IllegalArgumentException("A data/hora da mensagem nao pode ser posterior ao fim do chat.");
        }
    }

    private void validarRemetenteNoChat(Chat chat, Usuario remetente, Ajudante remetenteAjudante) {
        boolean remetenteUsuarioValido = remetente != null
                && chat.getUsuario() != null
                && chat.getUsuario().getId().equals(remetente.getId());
        boolean remetenteAjudanteValido = remetenteAjudante != null
                && chat.getAjudante() != null
                && chat.getAjudante().getId().equals(remetenteAjudante.getId());

        if (!(remetenteUsuarioValido ^ remetenteAjudanteValido)) {
            throw new IllegalArgumentException("O remetente informado nao pertence ao chat.");
        }
    }

    private void validarRemetenteDoUsuarioLogado(Usuario remetente, Ajudante remetenteAjudante) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        if (principal.isAdmin()) {
            return;
        }

        if (principal.isUsuario()) {
            if (principal.getUsuarioId() == null
                    || remetente == null
                    || !principal.getUsuarioId().equals(remetente.getId())) {
                throw new AccessDeniedException("Usuario nao pode enviar mensagem em nome de outro remetente.");
            }
            return;
        }

        if (principal.isAjudante()) {
            if (principal.getAjudanteId() == null
                    || remetenteAjudante == null
                    || !principal.getAjudanteId().equals(remetenteAjudante.getId())) {
                throw new AccessDeniedException("Ajudante nao pode enviar mensagem em nome de outro remetente.");
            }
            return;
        }

        throw new AccessDeniedException("Perfil sem permissao para envio de mensagem.");
    }

    private void validarAcessoAoChat(Chat chat) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        if (principal.isAdmin()) {
            return;
        }

        if (principal.isUsuario()
                && chat.getUsuario() != null
                && chat.getUsuario().getId().equals(principal.getUsuarioId())) {
            return;
        }

        if (principal.isAjudante()
                && chat.getAjudante() != null
                && chat.getAjudante().getId().equals(principal.getAjudanteId())) {
            return;
        }

        throw new AccessDeniedException("Acesso negado ao chat informado.");
    }

    private boolean isFinalizado(ChatStatus status) {
        return status == ChatStatus.FINALIZADO_USUARIO
                || status == ChatStatus.FINALIZADO_AJUDANTE
                || status == ChatStatus.FINALIZADO_SISTEMA;
    }

    private void exigirAdmin() {
        if (!authenticatedUserService.isAdmin()) {
            throw new AccessDeniedException("Apenas administradores podem executar esta operacao.");
        }
    }
}
