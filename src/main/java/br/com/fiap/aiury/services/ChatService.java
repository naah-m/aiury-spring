package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
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
    Chat criarChat(@Valid ChatRequestDTO chatDTO);

    /**
     * Abre um novo chat para o usuario autenticado.
     *
     * @param usuarioId identificador do usuario logado
     * @return chat persistido
     */
    Chat abrirChatParaUsuario(Long usuarioId);

    /**
     * Busca chat por identificador.
     *
     * @param id identificador do chat
     * @return chat encontrado
     */
    Chat buscarPorId(Long id);

    /**
     * Lista chats com filtros opcionais.
     *
     * @param usuarioId filtro opcional por usuario
     * @param ajudanteId filtro opcional por ajudante
     * @param status filtro opcional por status
     * @return colecao de chats
     */
    List<Chat> buscarTodos(Long usuarioId, Long ajudanteId, ChatStatus status);

    /**
     * Atualiza chat existente.
     *
     * @param id identificador do chat
     * @param chatDTO novos dados
     * @return chat atualizado
     */
    Chat atualizarChat(Long id, @Valid ChatRequestDTO chatDTO);

    /**
     * Exclui chat por ID.
     *
     * @param id identificador do chat
     */
    void deletarChat(Long id);
}
