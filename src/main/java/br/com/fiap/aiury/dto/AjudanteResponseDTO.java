package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de resposta do recurso de ajudante.
 */
@Data
@Schema(name = "AjudanteResponse", description = "Representação de ajudante retornada pela API")
public class AjudanteResponseDTO {

    @Schema(description = "Identificador do ajudante", example = "3")
    private Long id;

    @Schema(description = "Área principal de atuação", example = "Escuta ativa")
    private String areaAtuacao;

    @Schema(description = "Login de acesso do ajudante", example = "ajudante.escuta")
    private String login;

    @Schema(description = "Texto de motivação", example = "Atuo voluntariamente em acolhimento emocional")
    private String motivacao;

    @Schema(description = "Disponibilidade para novos atendimentos", example = "true")
    private Boolean disponivel;

    @Schema(description = "Média de avaliação", example = "4.9")
    private Double rating;
}
