package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para operacoes de cadastro e atualizacao de usuario.
 */
@Data
@Schema(name = "UsuarioRequest", description = "Payload para criacao/atualizacao de usuario")
public class UsuarioDTO {

    @NotBlank(message = "O nome e obrigatorio")
    @Schema(description = "Nome real da pessoa usuaria", example = "Maria Silva")
    private String nomeReal;

    @Schema(description = "Nome anonimo exibido no chat", example = "LuzInterior")
    private String nomeAnonimo;

    @NotBlank(message = "A data de nascimento e obrigatoria")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Formato invalido para data de nascimento. Use DD-MM-AAAA")
    @Schema(description = "Data de nascimento no formato DD-MM-AAAA", example = "15-08-1998")
    private String dataNascimento;

    @Schema(description = "Telefone celular para contato", example = "11999998888")
    private String celular;

    @NotBlank(message = "A senha e obrigatoria")
    @Size(min = 8, message = "A senha deve ter no minimo 8 caracteres")
    @Schema(description = "Senha de autenticacao", example = "segredo123")
    private String senha;

    @NotNull(message = "O ID da cidade e necessario para a localizacao")
    @Schema(description = "ID da cidade vinculada ao usuario", example = "1")
    private Long cidadeId;
}
