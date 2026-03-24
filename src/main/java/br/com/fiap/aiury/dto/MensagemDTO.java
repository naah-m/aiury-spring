package br.com.fiap.aiury.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MensagemDTO {

    private Long id;

    @NotNull(message = "O ID do chat e obrigatorio")
    private Long chatId;

    @NotNull(message = "O ID do remetente e obrigatorio")
    private Long remetenteId;

    @NotBlank(message = "O texto da mensagem e obrigatorio")
    @Size(max = 1000, message = "O texto da mensagem deve ter no maximo 1000 caracteres")
    private String texto;

    @NotNull(message = "A data de envio e obrigatoria")
    private LocalDateTime dataEnvio;
}
