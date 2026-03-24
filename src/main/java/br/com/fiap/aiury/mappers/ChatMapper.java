package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

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
