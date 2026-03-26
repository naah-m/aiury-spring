package br.com.fiap.aiury.dto.web;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AjudanteWebForm {

    @NotBlank(message = "A area de atuacao e obrigatoria")
    @Size(max = 100, message = "A area de atuacao deve ter no maximo 100 caracteres")
    private String areaAtuacao;

    @NotBlank(message = "O login e obrigatorio")
    @Size(max = 60, message = "O login deve ter no maximo 60 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "O login deve conter apenas letras, numeros, ponto, traco ou underline"
    )
    private String login;

    @Pattern(
            regexp = "^$|^.{8,255}$",
            message = "A senha deve ter entre 8 e 255 caracteres"
    )
    private String senha;

    @Size(max = 2000, message = "A motivacao deve ter no maximo 2000 caracteres")
    private String motivacao;

    @NotNull(message = "A disponibilidade e obrigatoria")
    private Boolean disponivel;

    @DecimalMin(value = "0.0", message = "O rating minimo e 0.0")
    @DecimalMax(value = "5.0", message = "O rating maximo e 5.0")
    private Double rating;
}
