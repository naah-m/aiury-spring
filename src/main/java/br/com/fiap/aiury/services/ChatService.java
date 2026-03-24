package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Chat;
import jakarta.validation.Valid;

import java.util.List;

public interface ChatService {

    Chat criarChat(@Valid ChatDTO chatDTO);

    Chat buscarPorId(Long id);

    List<Chat> buscarTodos();

    Chat atualizarChat(Long id, @Valid ChatDTO chatDTO);

    void deletarChat(Long id);
}
