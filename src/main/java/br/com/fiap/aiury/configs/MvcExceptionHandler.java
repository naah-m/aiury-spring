package br.com.fiap.aiury.configs;

import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "br.com.fiap.aiury.controller.mvc", annotations = Controller.class)
public class MvcExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Nao encontrado");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(ConflictException ex, Model model) {
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("error", "Conflito");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Requisicao invalida");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied() {
        return "error/access-denied";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Erro interno do servidor");
        model.addAttribute("message", "Nao foi possivel concluir a operacao.");
        return "error/default";
    }
}
