package br.com.fiap.aiury.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração central de metadados do Swagger/OpenAPI.
 */
@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Aiury API",
                version = "v1",
                description = "Documentacao oficial da API REST do projeto Aiury."
        )
)
@SecurityScheme(
        name = "sessionCookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "JSESSIONID",
        description = "Sessao autenticada via cookie de login da aplicacao web."
)
public class OpenApiConfig {

    @Bean
    public OpenAPI aiuryOpenAPI() {
        return new OpenAPI()
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(
                                "sessionCookie",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY)
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.COOKIE)
                                        .name("JSESSIONID")
                                        .description("Sessao autenticada via login da aplicacao web.")
                        )
                )
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("sessionCookie"))
                .info(new Info()
                        .title("Aiury API")
                        .description("""
                                API REST para acolhimento emocional com arquitetura em camadas, DTOs dedicados, HATEOAS e tratamento global de erros.

                                Padrao oficial de datas:
                                - Data: dd/MM/yyyy (ex.: 15/08/1998)
                                - Data/hora: dd/MM/yyyy HH:mm:ss (ex.: 25/03/2026 14:00:00)

                                Fluxo recomendado para testes manuais:
                                1. Criar estado
                                2. Criar cidade
                                3. Criar usuario
                                4. Criar ajudante
                                5. Criar chat
                                6. Criar mensagem
                                """)
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

