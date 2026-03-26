package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de resposta do recurso de mensagem.
 */
@Data
@Schema(name = "MensagemResponse", description = "Representacao de mensagem retornada pela API")
public class MensagemResponseDTO {

    @Schema(description = "Identificador da mensagem", example = "700")
    private Long id;

    @Schema(description = "ID do chat relacionado", example = "101")
    private Long chatId;

    @Schema(description = "ID do remetente", example = "10")
    private Long remetenteId;

    @Schema(description = "ID do remetente ajudante", example = "3")
    private Long remetenteAjudanteId;

    @Schema(description = "Tipo do remetente (USUARIO ou AJUDANTE)", example = "USUARIO")
    private String remetenteTipo;

    @Schema(description = "Texto enviado", example = "Obrigado pela escuta de hoje.")
    private String texto;

    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data e hora de envio no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 14:15:00",
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataEnvio;
}
