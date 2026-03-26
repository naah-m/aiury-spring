package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.ChatMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao de regras da camada de servico para chats.
 *
 * Responsabilidades:
 * - validar existencia de usuario e ajudante referenciados;
 * - aplicar fluxo de criacao/atualizacao/exclusao do chat;
 * - encapsular acesso aos repositorios.
 */
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final MensagemRepository mensagemRepository;
    private final AiuryAuthenticatedUserService authenticatedUserService;
    private final ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository,
                           UsuarioRepository usuarioRepository,
                           AjudanteRepository ajudanteRepository,
                           MensagemRepository mensagemRepository,
                           AiuryAuthenticatedUserService authenticatedUserService,
                           ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.mensagemRepository = mensagemRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.chatMapper = chatMapper;
    }

    /**
     * Cria chat apos validar usuario e ajudante informados.
     */
    @Override
    @Transactional
    public Chat criarChat(ChatRequestDTO chatDTO) {
        exigirAdmin();
        validarConsistenciaTemporal(chatDTO);
        Usuario usuario = buscarUsuarioPorId(chatDTO.getUsuarioId());
        Ajudante ajudante = buscarAjudantePorId(chatDTO.getAjudanteId());

        Chat chat = chatMapper.toEntity(chatDTO, usuario, ajudante);
        return chatRepository.save(chat);
    }

    /**
     * Busca chat por ID com tratamento de nao encontrado.
     */
    @Override
    public Chat buscarPorId(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat nao encontrado com ID: " + id));
        validarAcessoAoChat(chat);
        return chat;
    }

    /**
     * Lista chats com filtros opcionais.
     */
    @Override
    public List<Chat> buscarTodos(Long usuarioId, Long ajudanteId, ChatStatus status) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();

        Long usuarioIdEfetivo = usuarioId;
        Long ajudanteIdEfetivo = ajudanteId;

        if (principal.isUsuario()) {
            Long usuarioLogadoId = principal.getUsuarioId();
            if (usuarioLogadoId == null) {
                throw new AccessDeniedException("Perfil de usuario autenticado sem vinculo valido.");
            }
            if (usuarioId != null && !usuarioId.equals(usuarioLogadoId)) {
                throw new AccessDeniedException("O usuario logado nao possui acesso ao filtro informado.");
            }
            if (ajudanteId != null) {
                throw new AccessDeniedException("Nao e permitido filtrar por ajudante neste perfil.");
            }
            usuarioIdEfetivo = usuarioLogadoId;
            ajudanteIdEfetivo = null;
        }

        if (principal.isAjudante()) {
            Long ajudanteLogadoId = principal.getAjudanteId();
            if (ajudanteLogadoId == null) {
                throw new AccessDeniedException("Perfil de ajudante autenticado sem vinculo valido.");
            }
            if (ajudanteId != null && !ajudanteId.equals(ajudanteLogadoId)) {
                throw new AccessDeniedException("O ajudante logado nao possui acesso ao filtro informado.");
            }
            if (usuarioId != null) {
                throw new AccessDeniedException("Nao e permitido filtrar por usuario neste perfil.");
            }
            ajudanteIdEfetivo = ajudanteLogadoId;
            usuarioIdEfetivo = null;
        }

        Specification<Chat> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        final Long filtroUsuarioId = usuarioIdEfetivo;
        final Long filtroAjudanteId = ajudanteIdEfetivo;

        if (filtroUsuarioId != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("usuario").get("id"), filtroUsuarioId)
            );
        }

        if (filtroAjudanteId != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ajudante").get("id"), filtroAjudanteId)
            );
        }

        if (status != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status)
            );
        }

        return chatRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "dataInicio"));
    }

    /**
     * Atualiza chat existente validando as referencias relacionais.
     */
    @Override
    @Transactional
    public Chat atualizarChat(Long id, ChatRequestDTO chatDTO) {
        exigirAdmin();
        Chat chatExistente = buscarPorId(id);
        validarConsistenciaTemporal(chatDTO);

        Usuario usuario = buscarUsuarioPorId(chatDTO.getUsuarioId());
        Ajudante ajudante = buscarAjudantePorId(chatDTO.getAjudanteId());

        chatMapper.updateEntityFromDto(chatExistente, chatDTO, usuario, ajudante);
        return chatRepository.save(chatExistente);
    }

    /**
     * Exclui chat com verificacao previa de existencia.
     */
    @Override
    @Transactional
    public void deletarChat(Long id) {
        exigirAdmin();
        if (!chatRepository.existsById(id)) {
            throw new NotFoundException("Chat nao encontrado com ID: " + id);
        }

        try {
            mensagemRepository.deleteByChat_Id(id);
            chatRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Nao foi possivel excluir o chat pois existem dados vinculados.");
        }
    }

    /**
     * Busca usuario por ID para uso interno na composicao do chat.
     */
    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }

    /**
     * Busca ajudante por ID para uso interno na composicao do chat.
     */
    private Ajudante buscarAjudantePorId(Long id) {
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
    }

    private void exigirAdmin() {
        if (!authenticatedUserService.isAdmin()) {
            throw new AccessDeniedException("Apenas administradores podem executar esta operacao.");
        }
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

        throw new AccessDeniedException("Acesso negado ao chat solicitado.");
    }

    private void validarConsistenciaTemporal(ChatRequestDTO chatDTO) {
        if (chatDTO.getDataFim() != null && chatDTO.getDataFim().isBefore(chatDTO.getDataInicio())) {
            throw new IllegalArgumentException("A data/hora de fim nao pode ser anterior a data/hora de inicio.");
        }

        boolean statusFinalizado = chatDTO.getStatus() == ChatStatus.FINALIZADO_USUARIO
                || chatDTO.getStatus() == ChatStatus.FINALIZADO_AJUDANTE
                || chatDTO.getStatus() == ChatStatus.FINALIZADO_SISTEMA;

        if (statusFinalizado && chatDTO.getDataFim() == null) {
            throw new IllegalArgumentException("Chats finalizados devem informar data/hora de fim.");
        }

        if (chatDTO.getStatus() == ChatStatus.INICIADO && chatDTO.getDataFim() != null) {
            throw new IllegalArgumentException("Chat com status INICIADO nao pode ter data/hora de fim.");
        }
    }
}
