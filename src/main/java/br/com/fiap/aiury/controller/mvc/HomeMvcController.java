package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeMvcController {

    private final DashboardService dashboardService;

    public HomeMvcController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String root() {
        return "home/index";
    }

    @GetMapping("/app")
    public String dashboard(Model model) {
        model.addAttribute("summary", dashboardService.obterResumo());
        return "app/dashboard";
    }
}
