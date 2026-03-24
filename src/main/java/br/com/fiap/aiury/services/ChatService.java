package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Chat;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servicos para abertura e manutencao de chats.
 */
public interface ChatService {

    /**
     * Cria novo chat.
     *
     * @param chatDTO dados do chat
     * @return chat persistido
     */
    Chat criarChat(@Valid ChatDTO chatDTO);

    /**
     * Busca chat por identificador.
     *
     * @param id identificador do chat
     * @return chat encontrado
     */
    Chat buscarPorId(Long id);

    /**
     * Lista todos os chats.
     *
     * @return colecao de chats
     */
    List<Chat> buscarTodos();

    /**
     * Atualiza chat existente.
     *
     * @param id identificador do chat
     * @param chatDTO novos dados
     * @return chat atualizado
     */
    Chat atualizarChat(Long id, @Valid ChatDTO chatDTO);

    /**
     * Exclui chat por ID.
     *
     * @param id identificador do chat
     */
    void deletarChat(Long id);
}
