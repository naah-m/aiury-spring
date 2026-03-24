package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.entities.ChatStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDTO {

    private Long id;

    @NotNull(message = "O ID do usuario e obrigatorio")
    private Long usuarioId;

    @NotNull(message = "O ID do ajudante e obrigatorio")
    private Long ajudanteId;

    @NotNull(message = "A data de inicio e obrigatoria")
    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @NotNull(message = "O status do chat e obrigatorio")
    private ChatStatus status;
}
