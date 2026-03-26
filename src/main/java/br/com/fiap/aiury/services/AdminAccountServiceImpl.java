package br.com.fiap.aiury.services;

import br.com.fiap.aiury.entities.AdminAccount;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.repositories.AdminAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    private final AdminAccountRepository adminAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountServiceImpl(AdminAccountRepository adminAccountRepository, PasswordEncoder passwordEncoder) {
        this.adminAccountRepository = adminAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdminAccount criarContaSeAusente(String username, String senha) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(senha)) {
            throw new IllegalArgumentException("Credenciais de administrador invalidas.");
        }

        return adminAccountRepository.findByUsernameIgnoreCase(username)
                .orElseGet(() -> {
                    AdminAccount admin = new AdminAccount();
                    admin.setUsername(normalizarUsername(username));
                    admin.setSenha(codificarSenha(senha));
                    return adminAccountRepository.save(admin);
                });
    }

    @Override
    @Transactional
    public void alterarSenha(String username, String senhaAtual, String novaSenha) {
        AdminAccount admin = adminAccountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Conta de administrador nao encontrada."));

        if (!StringUtils.hasText(senhaAtual)) {
            throw new IllegalArgumentException("Informe a senha atual.");
        }
        if (!StringUtils.hasText(novaSenha)) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }

        if (!passwordEncoder.matches(senhaAtual, admin.getSenha())) {
            throw new IllegalArgumentException("A senha atual informada nao confere.");
        }
        if (passwordEncoder.matches(novaSenha, admin.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual.");
        }

        admin.setSenha(codificarSenha(novaSenha));
        adminAccountRepository.save(admin);
    }

    private String codificarSenha(String senha) {
        if (!StringUtils.hasText(senha)) {
            return senha;
        }

        String valorNormalizado = senha.trim();
        if (valorNormalizado.startsWith("$2a$")
                || valorNormalizado.startsWith("$2b$")
                || valorNormalizado.startsWith("$2y$")) {
            return valorNormalizado;
        }
        return passwordEncoder.encode(valorNormalizado);
    }

    private String normalizarUsername(String username) {
        return username.trim();
    }
}
