package br.com.fiap.aiury.dto.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensagemWebForm {

    @NotBlank(message = "Digite a mensagem antes de enviar")
    @Size(max = 1000, message = "A mensagem deve ter no maximo 1000 caracteres")
    private String texto;
}
