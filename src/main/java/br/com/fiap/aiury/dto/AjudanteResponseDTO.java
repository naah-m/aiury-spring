package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de resposta do recurso de ajudante.
 */
@Data
@Schema(name = "AjudanteResponse", description = "Representacao de ajudante retornada pela API")
public class AjudanteResponseDTO {

    @Schema(description = "Identificador do ajudante", example = "3")
    private Long id;

    @Schema(description = "Area principal de atuacao", example = "Escuta ativa")
    private String areaAtuacao;

    @Schema(description = "Texto de motivacao", example = "Atuo voluntariamente em acolhimento emocional")
    private String motivacao;

    @Schema(description = "Disponibilidade para novos atendimentos", example = "true")
    private Boolean disponivel;

    @Schema(description = "Media de avaliacao", example = "4.9")
    private Double rating;
}
