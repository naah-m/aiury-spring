package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de entrada para criacao e atualizacao de mensagem.
 */
@Data
@Schema(name = "MensagemRequest", description = "Payload de entrada para criacao/atualizacao de mensagem")
public class MensagemRequestDTO {

    @NotNull(message = "O ID do chat e obrigatorio")
    @Schema(description = "Identificador do chat ao qual a mensagem pertence", example = "101")
    private Long chatId;

    @Schema(description = "Identificador do usuario remetente", example = "10", nullable = true)
    private Long remetenteId;

    @Schema(description = "Identificador do ajudante remetente", example = "3", nullable = true)
    private Long remetenteAjudanteId;

    @NotBlank(message = "O texto da mensagem e obrigatorio")
    @Size(max = 1000, message = "O texto da mensagem deve ter no maximo 1000 caracteres")
    @Schema(description = "Conteudo textual enviado no chat", example = "Obrigado pela escuta de hoje.")
    private String texto;

    @NotNull(message = "A data de envio e obrigatoria")
    @JsonFormat(pattern = DateTimePatterns.DATE_TIME)
    @Schema(
            description = "Data e hora do envio no formato DD/MM/AAAA HH:mm:ss",
            example = "25/03/2026 14:15:00",
            type = "string",
            pattern = DateTimePatterns.DATE_TIME
    )
    private LocalDateTime dataEnvio;

    @AssertTrue(message = "Informe exatamente um remetente: usuario ou ajudante")
    public boolean isRemetenteValido() {
        boolean temUsuario = remetenteId != null;
        boolean temAjudante = remetenteAjudanteId != null;
        return temUsuario ^ temAjudante;
    }
}
