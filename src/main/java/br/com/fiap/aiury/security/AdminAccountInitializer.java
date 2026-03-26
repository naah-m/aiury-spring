package br.com.fiap.aiury.security;

import br.com.fiap.aiury.services.AdminAccountService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final AiuryAdminProperties adminProperties;
    private final AdminAccountService adminAccountService;

    public AdminAccountInitializer(AiuryAdminProperties adminProperties, AdminAccountService adminAccountService) {
        this.adminProperties = adminProperties;
        this.adminAccountService = adminAccountService;
    }

    @Override
    public void run(ApplicationArguments args) {
        adminAccountService.criarContaSeAusente(
                adminProperties.getUsername(),
                adminProperties.getPassword()
        );
    }
}
