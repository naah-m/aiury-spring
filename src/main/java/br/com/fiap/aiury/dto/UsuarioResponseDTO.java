package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de resposta do recurso de usuario.
 */
@Data
@Schema(name = "UsuarioResponse", description = "Representacao de usuario retornada pela API sem dados sensiveis")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador do usuario", example = "10")
    private Long id;

    @Schema(description = "Nome real da pessoa usuaria", example = "Maria Silva")
    private String nomeReal;

    @Schema(description = "Nome anonimo exibido no chat", example = "LuzInterior")
    private String nomeAnonimo;

    @Schema(description = "Data de nascimento em formato ISO", example = "1998-08-15")
    private LocalDate dataNascimento;

    @Schema(description = "Telefone celular", example = "11999998888")
    private String celular;

    @Schema(description = "Data de cadastro em formato ISO", example = "2026-03-24")
    private LocalDate dataCadastro;

    @Schema(description = "Identificador da cidade vinculada", example = "1")
    private Long cidadeId;
}
