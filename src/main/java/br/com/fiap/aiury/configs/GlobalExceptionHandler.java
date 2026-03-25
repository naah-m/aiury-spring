package br.com.fiap.aiury.configs;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global para padronizacao de respostas de erro da API.
 */
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildError(HttpStatus.BAD_REQUEST, "Erro de validacao nos campos informados.", request.getRequestURI(), validationErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            validationErrors.put(extractLeafPropertyPath(propertyPath), violation.getMessage());
        }
        return buildError(HttpStatus.BAD_REQUEST, "Erro de validacao nos parametros informados.", request.getRequestURI(), validationErrors);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        InvalidFormatException invalidFormat = findCause(ex, InvalidFormatException.class);
        if (invalidFormat != null) {
            return handleInvalidFormat(invalidFormat, request.getRequestURI());
        }
        return buildError(HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido ou mal formatado.", request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Parametro invalido: " + ex.getName() + ". Valor recebido: " + ex.getValue();
        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Violacao de integridade de dados.";
        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null) {
            message = message + " " + ex.getMostSpecificCause().getMessage();
        }
        return buildError(HttpStatus.CONFLICT, message, request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.", request.getRequestURI(), null);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors
    ) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors
        );
        return ResponseEntity.status(status).body(response);
    }

    private ResponseEntity<ApiErrorResponse> handleInvalidFormat(InvalidFormatException ex, String path) {
        String fieldName = extractFieldName(ex.getPath());
        Class<?> targetType = ex.getTargetType();
        Map<String, String> validationErrors = new HashMap<>();

        if (LocalDate.class.equals(targetType)) {
            putErrorIfFieldPresent(validationErrors, fieldName, "Formato de data invalido. Use " + DateTimePatterns.DATE + ".");
            return buildError(HttpStatus.BAD_REQUEST, "Formato de data invalido no corpo da requisicao.", path, validationErrorsOrNull(validationErrors));
        }

        if (LocalDateTime.class.equals(targetType)) {
            putErrorIfFieldPresent(validationErrors, fieldName, "Formato de data/hora invalido. Use " + DateTimePatterns.DATE_TIME + ".");
            return buildError(HttpStatus.BAD_REQUEST, "Formato de data/hora invalido no corpo da requisicao.", path, validationErrorsOrNull(validationErrors));
        }

        if (targetType != null && targetType.isEnum()) {
            putErrorIfFieldPresent(validationErrors, fieldName, "Valor invalido para enumeracao.");
            return buildError(HttpStatus.BAD_REQUEST, "Valor invalido para campo enumerado.", path, validationErrorsOrNull(validationErrors));
        }

        String message = fieldName == null
                ? "Valor invalido informado no corpo da requisicao."
                : "Valor invalido para o campo '" + fieldName + "'.";
        return buildError(HttpStatus.BAD_REQUEST, message, path, validationErrorsOrNull(validationErrors));
    }

    private void putErrorIfFieldPresent(Map<String, String> validationErrors, String fieldName, String message) {
        if (fieldName != null && !fieldName.isBlank()) {
            validationErrors.put(fieldName, message);
        }
    }

    private Map<String, String> validationErrorsOrNull(Map<String, String> validationErrors) {
        return validationErrors.isEmpty() ? null : validationErrors;
    }

    private String extractFieldName(java.util.List<JsonMappingException.Reference> path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        JsonMappingException.Reference lastReference = path.get(path.size() - 1);
        return lastReference.getFieldName();
    }

    private String extractLeafPropertyPath(String propertyPath) {
        int lastDot = propertyPath.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < propertyPath.length() - 1) {
            return propertyPath.substring(lastDot + 1);
        }
        return propertyPath;
    }

    private <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return type.cast(current);
            }
            current = current.getCause();
        }
        return null;
    }
}
