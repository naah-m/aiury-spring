package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.dto.web.AjudanteListItemView;
import br.com.fiap.aiury.dto.web.AjudanteWebForm;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.web.AjudanteWebMapper;
import br.com.fiap.aiury.services.AjudanteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/app/ajudantes")
public class AjudanteMvcController {

    private final AjudanteService ajudanteService;
    private final AjudanteWebMapper ajudanteWebMapper;

    public AjudanteMvcController(AjudanteService ajudanteService, AjudanteWebMapper ajudanteWebMapper) {
        this.ajudanteService = ajudanteService;
        this.ajudanteWebMapper = ajudanteWebMapper;
    }

    @GetMapping
    public String listar(Model model) {
        List<AjudanteListItemView> ajudantes = ajudanteService.buscarTodos(null).stream()
                .map(ajudanteWebMapper::toListItem)
                .toList();
        model.addAttribute("ajudantes", ajudantes);
        return "app/ajudantes/list";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        if (!model.containsAttribute("ajudanteForm")) {
            AjudanteWebForm form = new AjudanteWebForm();
            form.setDisponivel(Boolean.TRUE);
            model.addAttribute("ajudanteForm", form);
        }
        configurarModoCriacao(model);
        return "app/ajudantes/form";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("ajudanteForm")) {
            Ajudante ajudante = ajudanteService.buscarPorId(id);
            model.addAttribute("ajudanteForm", ajudanteWebMapper.toForm(ajudante));
        }
        configurarModoEdicao(model, id);
        return "app/ajudantes/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("ajudanteForm") AjudanteWebForm ajudanteForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            configurarModoCriacao(model);
            return "app/ajudantes/form";
        }

        try {
            ajudanteService.criarAjudante(ajudanteWebMapper.toRequestDto(ajudanteForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ajudante cadastrado com sucesso.");
            return "redirect:/app/ajudantes";
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            configurarModoCriacao(model);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/ajudantes/form";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("ajudanteForm") AjudanteWebForm ajudanteForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            configurarModoEdicao(model, id);
            return "app/ajudantes/form";
        }

        try {
            ajudanteService.atualizarAjudante(id, ajudanteWebMapper.toRequestDto(ajudanteForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ajudante atualizado com sucesso.");
            return "redirect:/app/ajudantes";
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            configurarModoEdicao(model, id);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/ajudantes/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ajudanteService.deletarAjudante(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ajudante excluido com sucesso.");
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }
        return "redirect:/app/ajudantes";
    }

    private void configurarModoCriacao(Model model) {
        model.addAttribute("modoEdicao", false);
        model.addAttribute("ajudanteId", null);
    }

    private void configurarModoEdicao(Model model, Long id) {
        model.addAttribute("modoEdicao", true);
        model.addAttribute("ajudanteId", id);
    }
}
