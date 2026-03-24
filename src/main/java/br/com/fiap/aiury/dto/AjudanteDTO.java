package br.com.fiap.aiury.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AjudanteDTO {

    @NotBlank(message = "A area de atuacao e obrigatoria")
    private String areaAtuacao;

    private String motivacao;

    @NotNull(message = "O campo disponivel e obrigatorio")
    private Boolean disponivel;

    private Double rating;
}
