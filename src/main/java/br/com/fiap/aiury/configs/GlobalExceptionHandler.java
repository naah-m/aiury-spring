package br.com.fiap.aiury.configs;

import br.com.fiap.aiury.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de excecoes da API.
 *
 * Papel na arquitetura:
 * - centraliza a traducao de excecoes para respostas HTTP padronizadas;
 * - evita repeticao de tratamento de erro nos controllers.
 *
 * Observacoes:
 * - erros de validacao retornam mapa simples (campo -> mensagem);
 * - erros de nao encontrado retornam estrutura detalhada com timestamp.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Converte {@link NotFoundException} em resposta HTTP 404.
     *
     * @param ex excecao disparada quando recurso nao foi localizado
     * @return payload padronizado de erro
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(NotFoundException ex) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                "API Endpoint"
        );
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    /**
     * Converte falhas de Bean Validation em HTTP 400.
     *
     * @param ex excecao contendo erros de validacao de request
     * @return mapa de erros indexado pelo nome do campo invalido
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // TODO: adicionar handlers para excecoes de seguranca (403), duplicacao (409), entre outros cenarios.

    /**
     * Payload interno para padronizacao dos retornos de erro.
     *
     * @param timestamp instante da geracao da resposta
     * @param status codigo HTTP
     * @param error descricao curta do erro
     * @param message detalhe funcional da falha
     * @param path referencia de contexto/endpoint
     */
    private record ErrorDetails(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path
    ) {}
}
