package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.configs.DateTimePatterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de entrada para criacao e atualizacao de usuario.
 */
@Data
@Schema(name = "UsuarioRequest", description = "Payload para criacao/atualizacao de usuario")
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome real e obrigatorio")
    @Size(max = 100, message = "O nome real deve ter no maximo 100 caracteres")
    @Schema(description = "Nome civil da pessoa usuaria", example = "Maria Silva")
    private String nomeReal;

    @Size(max = 50, message = "O nome anonimo deve ter no maximo 50 caracteres")
    @Schema(description = "Apelido publico utilizado nos chats", example = "LuzInterior")
    private String nomeAnonimo;

    @NotNull(message = "A data de nascimento e obrigatoria")
    @Past(message = "A data de nascimento deve ser anterior a data atual")
    @JsonFormat(pattern = DateTimePatterns.DATE)
    @Schema(
            description = "Data de nascimento no formato DD/MM/AAAA",
            example = "15/08/1998",
            type = "string",
            pattern = DateTimePatterns.DATE
    )
    private LocalDate dataNascimento;

    @NotBlank(message = "O celular e obrigatorio")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O celular deve conter apenas numeros com DDD (10 ou 11 digitos)"
    )
    @Schema(description = "Numero de celular com DDD", example = "11999998888")
    private String celular;

    @Pattern(
            regexp = "^$|^.{8,255}$",
            message = "A senha deve ter entre 8 e 255 caracteres"
    )
    @Schema(
            description = "Senha de acesso do usuario (obrigatoria no cadastro, opcional na edicao)",
            example = "segredo123",
            nullable = true
    )
    private String senha;

    @NotNull(message = "O ID da cidade e obrigatorio")
    @Schema(description = "Identificador da cidade do usuario", example = "1")
    private Long cidadeId;
}
