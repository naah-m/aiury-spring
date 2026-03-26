package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Schema(description = "Nome anônimo exibido no chat", example = "LuzInterior")
    private String nomeAnonimo;

    @JsonFormat(pattern = DateTimePatterns.DATE)
    @Schema(
            description = "Data de nascimento no formato DD/MM/AAAA",
            example = "15/08/1998",
            type = "string",
            pattern = DateTimePatterns.DATE
    )
    private LocalDate dataNascimento;

    @Schema(description = "Telefone celular", example = "11999998888")
    private String celular;

    @JsonFormat(pattern = DateTimePatterns.DATE)
    @Schema(
            description = "Data de cadastro no formato DD/MM/AAAA",
            example = "24/03/2026",
            type = "string",
            pattern = DateTimePatterns.DATE
    )
    private LocalDate dataCadastro;

    @Schema(description = "Identificador da cidade vinculada", example = "1")
    private Long cidadeId;
}

