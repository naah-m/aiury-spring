package br.com.fiap.aiury.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excecao de recurso não encontrado no dominio da aplicacao.
 *
 * Uso esperado:
 * - lancada nas camadas de servico quando um registro obrigatório não existe;
 * - convertida automaticamente em HTTP 404 pelo {@code GlobalExceptionHandler}.
 *
 * Observacao:
 * - a anotacao {@link ResponseStatus} mantem compatibilidade com uso direto
 *   da excecao sem handler explicito.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Cria excecao com mensagem funcional detalhada.
     *
     * @param message descricao da causa para diagnostico e retorno ao cliente
     */
    public NotFoundException(String message) {
        super(message);
    }
}

