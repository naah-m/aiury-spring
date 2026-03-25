package br.com.fiap.aiury.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
                        .description("API REST para acolhimento emocional com arquitetura em camadas, DTOs dedicados, HATEOAS e tratamento global de erros.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Equipe Aiury")
                                .email("equipe.aiury@fiap.com.br"))
                        .license(new License()
                                .name("Uso academico")
                                .url("https://www.fiap.com.br")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente local")
                ));
    }
}
