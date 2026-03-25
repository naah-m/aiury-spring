package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para criacao e atualizacao de estado.
 */
@Data
@Schema(name = "EstadoRequest", description = "Payload para criacao/atualizacao de estado")
public class EstadoRequestDTO {

    @NotBlank(message = "O nome do estado e obrigatorio")
    @Size(max = 50, message = "O nome do estado deve ter no maximo 50 caracteres")
    @Schema(description = "Nome oficial do estado", example = "Sao Paulo")
    private String nomeEstado;

    @NotBlank(message = "A UF e obrigatoria")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "A UF deve conter exatamente 2 letras")
    @Schema(description = "Sigla da unidade federativa", example = "SP")
    private String uf;
}
