package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotNull(message = "O ID do remetente e obrigatorio")
    @Schema(description = "Identificador do usuario remetente", example = "10")
    private Long remetenteId;

    @NotBlank(message = "O texto da mensagem e obrigatorio")
    @Size(max = 1000, message = "O texto da mensagem deve ter no maximo 1000 caracteres")
    @Schema(description = "Conteudo textual enviado no chat", example = "Obrigado pela escuta de hoje.")
    private String texto;

    @NotNull(message = "A data de envio e obrigatoria")
    @Schema(description = "Data e hora do envio", example = "2026-03-24T14:15:00")
    private LocalDateTime dataEnvio;
}
