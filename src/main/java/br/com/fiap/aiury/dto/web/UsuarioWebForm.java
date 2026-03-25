package br.com.fiap.aiury.dto.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UsuarioWebForm {

    @NotBlank(message = "O nome real e obrigatorio")
    @Size(max = 100, message = "O nome real deve ter no maximo 100 caracteres")
    private String nomeReal;

    @Size(max = 50, message = "O nome anonimo deve ter no maximo 50 caracteres")
    private String nomeAnonimo;

    @NotNull(message = "A data de nascimento e obrigatoria")
    @Past(message = "A data de nascimento deve ser anterior a data atual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O celular deve conter apenas numeros com DDD (10 ou 11 digitos)"
    )
    private String celular;

    @NotBlank(message = "A senha e obrigatoria")
    @Size(min = 8, max = 255, message = "A senha deve ter entre 8 e 255 caracteres")
    private String senha;

    @NotNull(message = "A cidade e obrigatoria")
    private Long cidadeId;
}
