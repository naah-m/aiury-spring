package br.com.fiap.aiury.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracao central de metadados do Swagger/OpenAPI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aiuryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aiury API")
                        .description("API REST para gestao de usuarios, ajudantes, chats e mensagens da plataforma Aiury.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipe Aiury")
                                .email("equipe.aiury@fiap.com.br"))
                        .license(new License()
                                .name("Uso academico")
                                .url("https://www.fiap.com.br")));
    }
}
