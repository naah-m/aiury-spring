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
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Não encontrado");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(ConflictException ex, Model model) {
        model.addAttribute("status", 409);
        model.addAttribute("error", "Conflito");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Requisição inválida");
        model.addAttribute("message", ex.getMessage());
        return "error/default";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied() {
        return "error/access-denied";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", "Erro interno do servidor");
        model.addAttribute("message", "Não foi possível concluir a operação.");
        return "error/default";
    }
}
