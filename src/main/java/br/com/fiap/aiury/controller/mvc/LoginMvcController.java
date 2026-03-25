package br.com.fiap.aiury.controller.mvc;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginMvcController {

    @GetMapping("/login")
    public String login(Authentication authentication,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/app";
        }

        if (logout != null) {
            model.addAttribute("mensagemSucesso", "Sessao encerrada com sucesso.");
        }

        if (error != null) {
            model.addAttribute("mensagemErro", "Usuario ou senha invalidos.");
        }

        return "auth/login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "error/access-denied";
    }
}
