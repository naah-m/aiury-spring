package br.com.fiap.aiury.services;

import br.com.fiap.aiury.entities.AdminAccount;

public interface AdminAccountService {

    AdminAccount criarContaSeAusente(String username, String senha);

    void alterarSenha(String username, String senhaAtual, String novaSenha);
}
