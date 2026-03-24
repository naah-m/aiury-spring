package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemDTO;
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

    @Override
    @Transactional
    public Mensagem criarMensagem(MensagemDTO mensagemDTO) {
        Chat chat = buscarChatPorId(mensagemDTO.getChatId());
        Usuario remetente = buscarUsuarioPorId(mensagemDTO.getRemetenteId());

        Mensagem mensagem = mensagemMapper.toEntity(mensagemDTO, chat, remetente);
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarPorId(Long id) {
        return mensagemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mensagem nao encontrada com ID: " + id));
    }

    @Override
    public List<Mensagem> buscarTodos() {
        return mensagemRepository.findAll();
    }

    @Override
    @Transactional
    public Mensagem atualizarMensagem(Long id, MensagemDTO mensagemDTO) {
        Mensagem mensagemExistente = buscarPorId(id);

        Chat chat = buscarChatPorId(mensagemDTO.getChatId());
        Usuario remetente = buscarUsuarioPorId(mensagemDTO.getRemetenteId());

        mensagemMapper.updateEntityFromDto(mensagemExistente, mensagemDTO, chat, remetente);
        return mensagemRepository.save(mensagemExistente);
    }

    @Override
    @Transactional
    public void deletarMensagem(Long id) {
        if (!mensagemRepository.existsById(id)) {
            throw new NotFoundException("Mensagem nao encontrada com ID: " + id);
        }
        mensagemRepository.deleteById(id);
    }

    private Chat buscarChatPorId(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat nao encontrado com ID: " + id));
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }
}
