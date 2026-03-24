package br.com.fiap.aiury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe de bootstrap da aplicacao Spring Boot.
 *
 * Papel na arquitetura:
 * - ponto de entrada unico para inicializacao do contexto Spring;
 * - habilita auto-configuracao, scan de componentes e configuracoes padrao.
 *
 * Observacao:
 * - nao contem regra de negocio; a responsabilidade e somente iniciar a API.
 */
@SpringBootApplication
public class AiuryApplication {

	/**
	 * Inicializa a aplicacao web.
	 *
	 * @param args argumentos de linha de comando opcionais
	 */
	public static void main(String[] args) {
		SpringApplication.run(AiuryApplication.class, args);
	}

}
