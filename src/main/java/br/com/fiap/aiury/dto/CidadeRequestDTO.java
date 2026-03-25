package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criacao e atualizacao de cidade.
 */
@Data
@Schema(name = "CidadeRequest", description = "Payload para criacao/atualizacao de cidade")
public class CidadeRequestDTO {

    @NotBlank(message = "O nome da cidade e obrigatorio")
    @Size(max = 100, message = "O nome da cidade deve ter no maximo 100 caracteres")
    @Schema(description = "Nome oficial da cidade", example = "Sao Paulo")
    private String nomeCidade;

    @NotNull(message = "O ID do estado e obrigatorio")
    @Schema(description = "Identificador do estado da cidade", example = "1")
    private Long estadoId;
}
