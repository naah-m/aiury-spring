package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criacao e atualizacao de ajudante.
 */
@Data
@Schema(name = "AjudanteRequest", description = "Payload para criacao/atualizacao de ajudante")
public class AjudanteRequestDTO {

    @NotBlank(message = "A area de atuacao e obrigatoria")
    @Size(max = 100, message = "A area de atuacao deve ter no maximo 100 caracteres")
    @Schema(description = "Area principal de atuacao", example = "Escuta ativa")
    private String areaAtuacao;

    @Size(max = 2000, message = "A motivacao deve ter no maximo 2000 caracteres")
    @Schema(description = "Motivacao para atuar na plataforma", example = "Atuo com escuta qualificada em acolhimento emocional")
    private String motivacao;

    @NotNull(message = "A disponibilidade e obrigatoria")
    @Schema(description = "Indica se o ajudante esta apto para novos chats", example = "true")
    private Boolean disponivel;

    @DecimalMin(value = "0.0", message = "O rating minimo e 0.0")
    @DecimalMax(value = "5.0", message = "O rating maximo e 5.0")
    @Schema(description = "Nota media do ajudante (0.0 a 5.0)", example = "4.7")
    private Double rating;
}
