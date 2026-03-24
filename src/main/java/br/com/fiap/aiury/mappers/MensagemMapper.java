package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MensagemMapper {

    public Mensagem toEntity(MensagemDTO mensagemDTO, Chat chat, Usuario remetente) {
        if (mensagemDTO == null) {
            return null;
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setChat(chat);
        mensagem.setRemetente(remetente);
        mensagem.setTexto(mensagemDTO.getTexto());
        mensagem.setDataEnvio(mensagemDTO.getDataEnvio());

        return mensagem;
    }

    public void updateEntityFromDto(Mensagem mensagem, MensagemDTO mensagemDTO, Chat chat, Usuario remetente) {
        if (chat != null) {
            mensagem.setChat(chat);
        }
        if (remetente != null) {
            mensagem.setRemetente(remetente);
        }
        if (mensagemDTO.getTexto() != null) {
            mensagem.setTexto(mensagemDTO.getTexto());
        }
        if (mensagemDTO.getDataEnvio() != null) {
            mensagem.setDataEnvio(mensagemDTO.getDataEnvio());
        }
    }

    public MensagemDTO toDto(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }

        MensagemDTO mensagemDTO = new MensagemDTO();
        mensagemDTO.setId(mensagem.getId());
        mensagemDTO.setChatId(mensagem.getChat() != null ? mensagem.getChat().getId() : null);
        mensagemDTO.setRemetenteId(mensagem.getRemetente() != null ? mensagem.getRemetente().getId() : null);
        mensagemDTO.setTexto(mensagem.getTexto());
        mensagemDTO.setDataEnvio(mensagem.getDataEnvio());

        return mensagemDTO;
    }
}
