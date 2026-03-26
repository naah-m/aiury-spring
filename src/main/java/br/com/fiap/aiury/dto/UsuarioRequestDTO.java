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
@Schema(name = "UsuarioRequest", description = "Payload para criação/atualização de usuário")
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome real é obrigatório")
    @Size(max = 100, message = "O nome real deve ter no máximo 100 caracteres")
    @Schema(description = "Nome civil da pessoa usuaria", example = "Maria Silva")
    private String nomeReal;

    @Size(max = 50, message = "O nome anônimo deve ter no máximo 50 caracteres")
    @Schema(description = "Apelido publico utilizado nos chats", example = "LuzInterior")
    private String nomeAnonimo;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser anterior à data atual")
    @JsonFormat(pattern = DateTimePatterns.DATE)
    @Schema(
            description = "Data de nascimento no formato DD/MM/AAAA",
            example = "15/08/1998",
            type = "string",
            pattern = DateTimePatterns.DATE
    )
    private LocalDate dataNascimento;

    @NotBlank(message = "O celular é obrigatório")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O celular deve conter apenas números com DDD (10 ou 11 dígitos)"
    )
    @Schema(description = "Numero de celular com DDD", example = "11999998888")
    private String celular;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
    @Schema(description = "Senha de acesso do usuário", example = "segredo123")
    private String senha;

    @NotNull(message = "O ID da cidade é obrigatório")
    @Schema(description = "Identificador da cidade do usuário", example = "1")
    private Long cidadeId;
}

