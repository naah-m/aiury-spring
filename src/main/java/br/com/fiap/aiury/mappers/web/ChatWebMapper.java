package br.com.fiap.aiury.mappers.web;

import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.web.ChatDetailView;
import br.com.fiap.aiury.dto.web.ChatListItemView;
import br.com.fiap.aiury.dto.web.ChatMensagemItemView;
import br.com.fiap.aiury.dto.web.ChatStatusWebForm;
import br.com.fiap.aiury.dto.web.ChatWebForm;
import br.com.fiap.aiury.dto.web.MensagemWebForm;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatWebMapper {

    public ChatRequestDTO toRequestDto(ChatWebForm form) {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setUsuarioId(form.getUsuarioId());
        dto.setAjudanteId(form.getAjudanteId());
        dto.setDataInicio(form.getDataInicio());
        dto.setDataFim(form.getDataFim());
        dto.setStatus(form.getStatus());
        return dto;
    }

    public ChatRequestDTO toStatusUpdateRequest(Chat chat, ChatStatusWebForm form) {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setUsuarioId(chat.getUsuario() != null ? chat.getUsuario().getId() : null);
        dto.setAjudanteId(chat.getAjudante() != null ? chat.getAjudante().getId() : null);
        dto.setDataInicio(chat.getDataInicio());
        dto.setDataFim(form.getDataFim());
        dto.setStatus(form.getStatus());
        return dto;
    }

    public ChatListItemView toListItem(Chat chat) {
        Usuario usuario = chat.getUsuario();
        Ajudante ajudante = chat.getAjudante();

        return new ChatListItemView(
                chat.getId(),
                usuario != null ? usuario.getId() : null,
                usuario != null ? textoOuHifen(usuario.getNomeReal()) : "-",
                usuario != null ? textoOuHifen(usuario.getNomeAnonimo()) : "-",
                ajudante != null ? ajudante.getId() : null,
                ajudante != null ? textoOuHifen(ajudante.getAreaAtuacao()) : "-",
                ajudante != null && ajudante.isDisponivel(),
                chat.getDataInicio(),
                chat.getDataFim(),
                chat.getStatus()
        );
    }

    public ChatDetailView toDetailView(Chat chat) {
        Usuario usuario = chat.getUsuario();
        Cidade cidade = usuario != null ? usuario.getCidade() : null;
        Estado estado = cidade != null ? cidade.getEstado() : null;
        Ajudante ajudante = chat.getAjudante();

        return new ChatDetailView(
                chat.getId(),
                usuario != null ? usuario.getId() : null,
                usuario != null ? textoOuHifen(usuario.getNomeReal()) : "-",
                usuario != null ? textoOuHifen(usuario.getNomeAnonimo()) : "-",
                cidade != null ? textoOuHifen(cidade.getNomeCidade()) : "-",
                estado != null ? textoOuHifen(estado.getUf()) : "-",
                ajudante != null ? ajudante.getId() : null,
                ajudante != null ? textoOuHifen(ajudante.getAreaAtuacao()) : "-",
                ajudante != null && ajudante.isDisponivel(),
                ajudante != null ? ajudante.getRating() : null,
                chat.getDataInicio(),
                chat.getDataFim(),
                chat.getStatus()
        );
    }

    public ChatMensagemItemView toMensagemItem(Mensagem mensagem) {
        return new ChatMensagemItemView(
                mensagem.getId(),
                mensagem.getTexto(),
                mensagem.getDataEnvio()
        );
    }

    public MensagemRequestDTO toMensagemRequest(Chat chat, MensagemWebForm form, LocalDateTime dataEnvio) {
        MensagemRequestDTO dto = new MensagemRequestDTO();
        dto.setChatId(chat.getId());
        dto.setRemetenteId(chat.getUsuario() != null ? chat.getUsuario().getId() : null);
        dto.setTexto(form.getTexto());
        dto.setDataEnvio(dataEnvio);
        return dto;
    }

    private String textoOuHifen(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }
}
