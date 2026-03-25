package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.dto.ChatResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper para transformacoes entre entidade {@link Chat} e DTOs de chat.
 *
 * Observacao:
 * - as referencias de usuario e ajudante sao resolvidas na camada de servico
 *   e recebidas prontas pelo mapper.
 */
@Component
public class ChatMapper {

    /**
     * Converte DTO em entidade de chat.
     *
     * @param request dados do chat
     * @param usuario referencia do usuario resolvida no servico
     * @param ajudante referencia do ajudante resolvida no servico
     * @return entidade pronta para persistencia
     */
    public Chat toEntity(ChatRequestDTO request, Usuario usuario, Ajudante ajudante) {
        if (request == null) {
            return null;
        }

        Chat chat = new Chat();
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setDataInicio(request.getDataInicio());
        chat.setDataFim(request.getDataFim());
        chat.setStatus(request.getStatus());

        return chat;
    }

    /**
     * Atualiza chat existente com dados recebidos no DTO.
     *
     * @param chat entidade alvo
     * @param request novos dados
     * @param usuario usuario resolvido para relacao
     * @param ajudante ajudante resolvido para relacao
     */
    public void updateEntityFromDto(Chat chat, ChatRequestDTO request, Usuario usuario, Ajudante ajudante) {
        if (usuario != null) {
            chat.setUsuario(usuario);
        }
        if (ajudante != null) {
            chat.setAjudante(ajudante);
        }
        if (request.getDataInicio() != null) {
            chat.setDataInicio(request.getDataInicio());
        }
        chat.setDataFim(request.getDataFim());
        if (request.getStatus() != null) {
            chat.setStatus(request.getStatus());
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     *
     * @param chat entidade de dominio
     * @return DTO simplificado para camada HTTP
     */
    public ChatResponseDTO toResponseDto(Chat chat) {
        if (chat == null) {
            return null;
        }

        ChatResponseDTO response = new ChatResponseDTO();
        response.setId(chat.getId());
        response.setUsuarioId(chat.getUsuario() != null ? chat.getUsuario().getId() : null);
        response.setAjudanteId(chat.getAjudante() != null ? chat.getAjudante().getId() : null);
        response.setDataInicio(chat.getDataInicio());
        response.setDataFim(chat.getDataFim());
        response.setStatus(chat.getStatus());

        return response;
    }
}
