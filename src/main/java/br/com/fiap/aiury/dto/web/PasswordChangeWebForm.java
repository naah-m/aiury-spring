package br.com.fiap.aiury.dto.web;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeWebForm {

    @NotBlank(message = "Informe a senha atual.")
    private String senhaAtual;

    @NotBlank(message = "Informe a nova senha.")
    @Size(min = 8, max = 255, message = "A nova senha deve ter entre 8 e 255 caracteres.")
    private String novaSenha;

    @NotBlank(message = "Confirme a nova senha.")
    private String confirmacaoNovaSenha;

    @AssertTrue(message = "A confirmacao da nova senha nao confere.")
    public boolean isConfirmacaoValida() {
        if (novaSenha == null || confirmacaoNovaSenha == null) {
            return true;
        }
        return novaSenha.equals(confirmacaoNovaSenha);
    }
}
