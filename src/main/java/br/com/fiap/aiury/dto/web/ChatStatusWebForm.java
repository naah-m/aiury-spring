package br.com.fiap.aiury.dto.web;

import br.com.fiap.aiury.entities.ChatStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ChatStatusWebForm {

    @NotNull(message = "Selecione um status para atualizacao")
    private ChatStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataFim;

    @AssertTrue(message = "Chats finalizados devem informar data/hora de fim")
    public boolean isDataFimObrigatoriaQuandoFinalizado() {
        if (status == null) {
            return true;
        }
        return !isStatusFinalizado(status) || dataFim != null;
    }

    @AssertTrue(message = "Status INICIADO nao permite data/hora de fim")
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
