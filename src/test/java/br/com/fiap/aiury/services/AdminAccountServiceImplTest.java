package br.com.fiap.aiury.services;

import br.com.fiap.aiury.entities.AdminAccount;
import br.com.fiap.aiury.repositories.AdminAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAccountServiceImplTest {

    @Mock
    private AdminAccountRepository adminAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminAccountServiceImpl adminAccountService;

    @Test
    void deveCriarContaAdminQuandoNaoExiste() {
        when(adminAccountRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin123")).thenReturn("hash-admin");
        when(adminAccountRepository.save(any(AdminAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdminAccount admin = adminAccountService.criarContaSeAusente("admin", "admin123");

        assertThat(admin.getUsername()).isEqualTo("admin");
        assertThat(admin.getSenha()).isEqualTo("hash-admin");
    }

    @Test
    void deveAlterarSenhaDoAdminQuandoSenhaAtualValida() {
        AdminAccount admin = new AdminAccount();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setSenha("hash-atual");

        when(adminAccountRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("admin123", "hash-atual")).thenReturn(true);
        when(passwordEncoder.matches("adminNova123", "hash-atual")).thenReturn(false);
        when(passwordEncoder.encode("adminNova123")).thenReturn("hash-nova");

        adminAccountService.alterarSenha("admin", "admin123", "adminNova123");

        assertThat(admin.getSenha()).isEqualTo("hash-nova");
        verify(adminAccountRepository).save(admin);
    }

    @Test
    void deveLancarErroQuandoSenhaAtualAdminNaoConfere() {
        AdminAccount admin = new AdminAccount();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setSenha("hash-atual");

        when(adminAccountRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("errada", "hash-atual")).thenReturn(false);

        assertThatThrownBy(() -> adminAccountService.alterarSenha("admin", "errada", "adminNova123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao confere");
    }
}
