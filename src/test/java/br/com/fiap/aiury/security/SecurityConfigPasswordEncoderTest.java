package br.com.fiap.aiury.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SecurityConfigPasswordEncoderTest {

    @Test
    void deveAceitarApenasHashBCrypt() {
        SecurityConfig securityConfig = new SecurityConfig(mock(RoleBasedAuthenticationSuccessHandler.class));
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        String hashed = encoder.encode("segredo123");

        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(encoder.matches("segredo123", hashed)).isTrue();
        assertThat(hashed).isNotEqualTo("segredo123");
    }

    @Test
    void deveGerarHashBCryptComPrefixoEsperado() {
        SecurityConfig securityConfig = new SecurityConfig(mock(RoleBasedAuthenticationSuccessHandler.class));
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        String hashed = encoder.encode("outraSenha123");

        assertThat(hashed).startsWith("$2");
    }
}
