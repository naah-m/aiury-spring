package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.MensagemMapper;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MensagemMapper mensagemMapper;

    @Autowired
    public MensagemServiceImpl(MensagemRepository mensagemRepository,
                               ChatRepository chatRepository,
                               UsuarioRepository usuarioRepository,
                               MensagemMapper mensagemMapper) {
        this.mensagemRepository = mensagemRepository;
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
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

        Mensagem mensagem = mensagemMapper.toEntity(mensagemDTO, chat, remetente);
        return mensagemRepository.save(mensagem);
    }

    /**
     * Busca mensagem por ID com tratamento de nao encontrado.
     */
    @Override
    public Mensagem buscarPorId(Long id) {
        return mensagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mensagem nao encontrada com ID: " + id));
    }

    /**
     * Lista mensagens com filtros opcionais por chat/remetente.
     */
    @Override
    public List<Mensagem> buscarTodos(Long chatId, Long remetenteId) {
        if (chatId != null && remetenteId != null) {
            return mensagemRepository.findByChat_IdAndRemetente_IdOrderByDataEnvioAsc(chatId, remetenteId);
        }
        if (chatId != null) {
            return mensagemRepository.findByChat_IdOrderByDataEnvioAsc(chatId);
        }
        if (remetenteId != null) {
            return mensagemRepository.findByRemetente_IdOrderByDataEnvioAsc(remetenteId);
        }
        return mensagemRepository.findAll();
    }

    /**
     * Atualiza mensagem existente mantendo validacao de referencias.
     */
    @Override
    @Transactional
    public Mensagem atualizarMensagem(Long id, MensagemRequestDTO mensagemDTO) {
        Mensagem mensagemExistente = buscarPorId(id);

        Chat chat = buscarChatPorId(mensagemDTO.getChatId());
        Usuario remetente = buscarUsuarioPorId(mensagemDTO.getRemetenteId());

        mensagemMapper.updateEntityFromDto(mensagemExistente, mensagemDTO, chat, remetente);
        return mensagemRepository.save(mensagemExistente);
    }

    /**
     * Exclui mensagem por ID com validacao de existencia.
     */
    @Override
    @Transactional
    public void deletarMensagem(Long id) {
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
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }
}
