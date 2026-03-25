package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de resposta do recurso de estado.
 */
@Data
@Schema(name = "EstadoResponse", description = "Representacao de estado retornada pela API")
public class EstadoResponseDTO {

    @Schema(description = "Identificador do estado", example = "1")
    private Long id;

    @Schema(description = "Nome oficial do estado", example = "Sao Paulo")
    private String nomeEstado;

    @Schema(description = "Sigla da unidade federativa", example = "SP")
    private String uf;
}
