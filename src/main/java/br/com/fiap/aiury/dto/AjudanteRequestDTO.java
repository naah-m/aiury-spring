package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criação e atualização de ajudante.
 */
@Data
@Schema(name = "AjudanteRequest", description = "Payload para criação/atualização de ajudante")
public class AjudanteRequestDTO {

    @NotBlank(message = "A área de atuação é obrigatória")
    @Size(max = 100, message = "A área de atuação deve ter no máximo 100 caracteres")
    @Schema(description = "Área principal de atuação", example = "Escuta ativa")
    private String areaAtuacao;

    @NotBlank(message = "O login do ajudante é obrigatório")
    @Size(max = 60, message = "O login deve ter no máximo 60 caracteres")
    @Schema(description = "Login de acesso do ajudante", example = "ajudante.escuta")
    private String login;

    @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
    @Schema(
            description = "Senha do ajudante (obrigatória no cadastro, opcional na edição)",
            example = "ajudante123",
            nullable = true
    )
    private String senha;

    @Size(max = 2000, message = "A motivação deve ter no máximo 2000 caracteres")
    @Schema(description = "Motivação para atuar na plataforma", example = "Atuo com escuta qualificada em acolhimento emocional")
    private String motivacao;

    @NotNull(message = "A disponibilidade é obrigatória")
    @Schema(description = "Indica se o ajudante está apto para novos chats", example = "true")
    private Boolean disponivel;

    @DecimalMin(value = "0.0", message = "A avaliação mínima é 0.0")
    @DecimalMax(value = "5.0", message = "A avaliação máxima é 5.0")
    @Schema(description = "Avaliação média do ajudante (0.0 a 5.0)", example = "4.7")
    private Double rating;
}
