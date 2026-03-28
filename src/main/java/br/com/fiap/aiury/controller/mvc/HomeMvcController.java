package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.services.DashboardService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeMvcController {

    private static final DateTimeFormatter DASHBOARD_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DashboardService dashboardService;

    public HomeMvcController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/app";
        }
        return "home/index";
    }

    @GetMapping("/app")
    public String dashboard(Model model) {
        model.addAttribute("summary", dashboardService.obterResumo());
        model.addAttribute("generatedAtLabel", LocalDateTime.now().format(DASHBOARD_TIMESTAMP_FORMATTER));
        return "app/dashboard";
    }
}
