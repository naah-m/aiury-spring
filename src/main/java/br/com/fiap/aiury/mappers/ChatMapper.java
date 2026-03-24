package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper para transformacoes entre entidade {@link Chat} e {@link ChatDTO}.
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
     * @param chatDTO dados do chat
     * @param usuario referencia do usuario resolvida no servico
     * @param ajudante referencia do ajudante resolvida no servico
     * @return entidade pronta para persistencia
     */
    public Chat toEntity(ChatDTO chatDTO, Usuario usuario, Ajudante ajudante) {
        if (chatDTO == null) {
            return null;
        }

        Chat chat = new Chat();
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setDataInicio(chatDTO.getDataInicio());
        chat.setDataFim(chatDTO.getDataFim());
        chat.setStatus(chatDTO.getStatus());

        return chat;
    }

    /**
     * Atualiza chat existente com dados recebidos no DTO.
     *
     * @param chat entidade alvo
     * @param chatDTO novos dados
     * @param usuario usuario resolvido para relacao
     * @param ajudante ajudante resolvido para relacao
     */
    public void updateEntityFromDto(Chat chat, ChatDTO chatDTO, Usuario usuario, Ajudante ajudante) {
        if (usuario != null) {
            chat.setUsuario(usuario);
        }
        if (ajudante != null) {
            chat.setAjudante(ajudante);
        }
        if (chatDTO.getDataInicio() != null) {
            chat.setDataInicio(chatDTO.getDataInicio());
        }
        chat.setDataFim(chatDTO.getDataFim());
        if (chatDTO.getStatus() != null) {
            chat.setStatus(chatDTO.getStatus());
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     *
     * @param chat entidade de dominio
     * @return DTO simplificado para camada HTTP
     */
    public ChatDTO toDto(Chat chat) {
        if (chat == null) {
            return null;
        }

        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setUsuarioId(chat.getUsuario() != null ? chat.getUsuario().getId() : null);
        chatDTO.setAjudanteId(chat.getAjudante() != null ? chat.getAjudante().getId() : null);
        chatDTO.setDataInicio(chat.getDataInicio());
        chatDTO.setDataFim(chat.getDataFim());
        chatDTO.setStatus(chat.getStatus());

        return chatDTO;
    }
}
