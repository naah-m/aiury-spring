package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.ChatMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final ChatMapper chatMapper;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository,
                           UsuarioRepository usuarioRepository,
                           AjudanteRepository ajudanteRepository,
                           ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.chatMapper = chatMapper;
    }

    @Override
    @Transactional
    public Chat criarChat(ChatDTO chatDTO) {
        Usuario usuario = buscarUsuarioPorId(chatDTO.getUsuarioId());
        Ajudante ajudante = buscarAjudantePorId(chatDTO.getAjudanteId());

        Chat chat = chatMapper.toEntity(chatDTO, usuario, ajudante);
        return chatRepository.save(chat);
    }

    @Override
    public Chat buscarPorId(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat nao encontrado com ID: " + id));
    }

    @Override
    public List<Chat> buscarTodos() {
        return chatRepository.findAll();
    }

    @Override
    @Transactional
    public Chat atualizarChat(Long id, ChatDTO chatDTO) {
        Chat chatExistente = buscarPorId(id);

        Usuario usuario = buscarUsuarioPorId(chatDTO.getUsuarioId());
        Ajudante ajudante = buscarAjudantePorId(chatDTO.getAjudanteId());

        chatMapper.updateEntityFromDto(chatExistente, chatDTO, usuario, ajudante);
        return chatRepository.save(chatExistente);
    }

    @Override
    @Transactional
    public void deletarChat(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new NotFoundException("Chat nao encontrado com ID: " + id);
        }
        chatRepository.deleteById(id);
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }

    private Ajudante buscarAjudantePorId(Long id) {
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
    }
}
