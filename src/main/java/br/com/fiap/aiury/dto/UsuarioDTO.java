package br.com.fiap.aiury.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nomeReal;

    private String nomeAnonimo;

    @NotBlank(message = "A data de nascimento é obrigatória")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Formato inválido. Use DD-MM-AAAA")
    private String dataNascimento;

    private String celular;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotNull(message = "O ID da cidade é necessário para a localização")
    private Long cidadeId;
}
