package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO de resposta do recurso de cidade.
 */
@Data
@Schema(name = "CidadeResponse", description = "Representacao de cidade retornada pela API")
public class CidadeResponseDTO {

    @Schema(description = "Identificador da cidade", example = "10")
    private Long id;

    @Schema(description = "Nome da cidade", example = "Sao Paulo")
    private String nomeCidade;

    @Schema(description = "Identificador do estado vinculado", example = "1")
    private Long estadoId;

    @Schema(description = "Nome do estado vinculado", example = "Sao Paulo")
    private String estadoNome;

    @Schema(description = "UF do estado vinculado", example = "SP")
    private String estadoUf;
}
