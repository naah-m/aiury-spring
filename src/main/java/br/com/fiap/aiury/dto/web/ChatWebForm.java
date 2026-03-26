package br.com.fiap.aiury.dto.web;

import br.com.fiap.aiury.entities.ChatStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ChatWebForm {

    @NotNull(message = "Selecione o usuário para abrir o chat")
    private Long usuarioId;

    @NotNull(message = "Selecione o ajudante para abrir o chat")
    private Long ajudanteId;

    @NotNull(message = "A data/hora de início é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataFim;

    @NotNull(message = "Selecione o status inicial do chat")
    private ChatStatus status;

    @AssertTrue(message = "A data/hora de fim não pode ser anterior ao início")
    public boolean isConsistenciaTemporalValida() {
        if (dataInicio == null || dataFim == null) {
            return true;
        }
        return !dataFim.isBefore(dataInicio);
    }

    @AssertTrue(message = "Chats finalizados devem informar data/hora de fim")
    public boolean isDataFimObrigatoriaQuandoFinalizado() {
        if (status == null) {
            return true;
        }
        return !isStatusFinalizado(status) || dataFim != null;
    }

    @AssertTrue(message = "Chat com status INICIADO não pode informar data/hora de fim")
    public boolean isIniciadoSemDataFim() {
        if (status == null) {
            return true;
        }
        return status != ChatStatus.INICIADO || dataFim == null;
    }

    private boolean isStatusFinalizado(ChatStatus status) {
        return status == ChatStatus.FINALIZADO_USUARIO
                || status == ChatStatus.FINALIZADO_AJUDANTE
                || status == ChatStatus.FINALIZADO_SISTEMA;
    }
}
