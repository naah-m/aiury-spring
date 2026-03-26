package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.dto.web.PasswordChangeWebForm;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import br.com.fiap.aiury.services.AdminAccountService;
import br.com.fiap.aiury.services.AjudanteService;
import br.com.fiap.aiury.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/minha-conta")
public class AccountMvcController {

    private final AiuryAuthenticatedUserService authenticatedUserService;
    private final AdminAccountService adminAccountService;
    private final UsuarioService usuarioService;
    private final AjudanteService ajudanteService;

    public AccountMvcController(AiuryAuthenticatedUserService authenticatedUserService,
                                AdminAccountService adminAccountService,
                                UsuarioService usuarioService,
                                AjudanteService ajudanteService) {
        this.authenticatedUserService = authenticatedUserService;
        this.adminAccountService = adminAccountService;
        this.usuarioService = usuarioService;
        this.ajudanteService = ajudanteService;
    }

    @GetMapping
    public String exibirMinhaConta(Model model) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        validarPerfilElegivel(principal);

        if (!model.containsAttribute("passwordChangeForm")) {
            model.addAttribute("passwordChangeForm", new PasswordChangeWebForm());
        }

        adicionarContextoDaConta(model, principal);
        return "app/account/my-account";
    }

    @PostMapping("/senha")
    public String alterarSenha(@Valid @ModelAttribute("passwordChangeForm") PasswordChangeWebForm form,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        validarPerfilElegivel(principal);

        if (bindingResult.hasErrors()) {
            adicionarContextoDaConta(model, principal);
            return "app/account/my-account";
        }

        try {
            alterarSenhaDoPerfilAutenticado(principal, form);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Senha alterada com sucesso.");
            return "redirect:/app/minha-conta";
        } catch (IllegalArgumentException | NotFoundException ex) {
            adicionarContextoDaConta(model, principal);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/account/my-account";
        }
    }

    private void alterarSenhaDoPerfilAutenticado(AiuryUserPrincipal principal, PasswordChangeWebForm form) {
        if (principal.isAdmin()) {
            adminAccountService.alterarSenha(principal.getUsername(), form.getSenhaAtual(), form.getNovaSenha());
            return;
        }

        if (principal.isUsuario()) {
            Long usuarioId = principal.getUsuarioId();
            if (usuarioId == null) {
                throw new AccessDeniedException("Perfil de usuario sem vinculo valido.");
            }
            usuarioService.alterarSenha(usuarioId, form.getSenhaAtual(), form.getNovaSenha());
            return;
        }

        if (principal.isAjudante()) {
            Long ajudanteId = principal.getAjudanteId();
            if (ajudanteId == null) {
                throw new AccessDeniedException("Perfil de ajudante sem vinculo valido.");
            }
            ajudanteService.alterarSenha(ajudanteId, form.getSenhaAtual(), form.getNovaSenha());
            return;
        }

        throw new AccessDeniedException("Perfil sem permissao para alterar senha neste fluxo.");
    }

    private void validarPerfilElegivel(AiuryUserPrincipal principal) {
        if (!principal.isAdmin() && !principal.isUsuario() && !principal.isAjudante()) {
            throw new AccessDeniedException("Acesso permitido apenas para perfis autenticados validos.");
        }
    }

    private void adicionarContextoDaConta(Model model, AiuryUserPrincipal principal) {
        model.addAttribute("perfilAdmin", principal.isAdmin());
        model.addAttribute("perfilUsuario", principal.isUsuario());
        model.addAttribute("perfilAjudante", principal.isAjudante());
        model.addAttribute("loginAtual", principal.getUsername());
    }
}
