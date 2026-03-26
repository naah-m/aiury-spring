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

    @NotBlank(message = "A área de atuação é obrigatória")
    @Size(max = 100, message = "A área de atuação deve ter no máximo 100 caracteres")
    private String areaAtuacao;

    @NotBlank(message = "O login é obrigatório")
    @Size(max = 60, message = "O login deve ter no máximo 60 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "O login deve conter apenas letras, números, ponto, traço ou underline"
    )
    private String login;

    @Pattern(
            regexp = "^$|^.{8,255}$",
            message = "A senha deve ter entre 8 e 255 caracteres"
    )
    private String senha;

    @Size(max = 2000, message = "A motivação deve ter no máximo 2000 caracteres")
    private String motivacao;

    @NotNull(message = "A disponibilidade é obrigatória")
    private Boolean disponivel;

    @DecimalMin(value = "0.0", message = "A avaliação mínima é 0.0")
    @DecimalMax(value = "5.0", message = "A avaliação máxima é 5.0")
    private Double rating;
}
