package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.dto.web.UsuarioListItemView;
import br.com.fiap.aiury.dto.web.UsuarioWebForm;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.web.UsuarioWebMapper;
import br.com.fiap.aiury.services.CidadeService;
import br.com.fiap.aiury.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/app/usuarios")
public class UsuarioMvcController {

    private final UsuarioService usuarioService;
    private final CidadeService cidadeService;
    private final UsuarioWebMapper usuarioWebMapper;

    public UsuarioMvcController(UsuarioService usuarioService,
                                CidadeService cidadeService,
                                UsuarioWebMapper usuarioWebMapper) {
        this.usuarioService = usuarioService;
        this.cidadeService = cidadeService;
        this.usuarioWebMapper = usuarioWebMapper;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long cidadeId, Model model) {
        List<Usuario> usuarios = usuarioService.buscarTodos(cidadeId);
        List<UsuarioListItemView> usuarioViews = usuarios.stream()
                .map(usuarioWebMapper::toListItem)
                .toList();

        model.addAttribute("usuarios", usuarioViews);
        model.addAttribute("filtroCidadeId", cidadeId);
        model.addAttribute("cidades", cidadeService.buscarTodos(null));
        return "app/usuarios/list";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        if (!model.containsAttribute("usuarioForm")) {
            model.addAttribute("usuarioForm", new UsuarioWebForm());
        }
        configurarModoCriacao(model);
        carregarCidades(model);
        return "app/usuarios/form";
    }

    @GetMapping("/{id}/editar")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("usuarioForm")) {
            Usuario usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuarioForm", usuarioWebMapper.toForm(usuario));
        }

        configurarModoEdicao(model, id);
        carregarCidades(model);
        return "app/usuarios/form";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("usuarioForm") UsuarioWebForm usuarioForm,
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            configurarModoCriacao(model);
            carregarCidades(model);
            return "app/usuarios/form";
        }

        try {
            usuarioService.criarUsuario(usuarioWebMapper.toRequestDto(usuarioForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário cadastrado com sucesso.");
            return "redirect:/app/usuarios";
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            configurarModoCriacao(model);
            carregarCidades(model);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/usuarios/form";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("usuarioForm") UsuarioWebForm usuarioForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            configurarModoEdicao(model, id);
            carregarCidades(model);
            return "app/usuarios/form";
        }

        try {
            usuarioService.atualizarUsuario(id, usuarioWebMapper.toRequestDto(usuarioForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso.");
            return "redirect:/app/usuarios";
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            configurarModoEdicao(model, id);
            carregarCidades(model);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/usuarios/form";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário excluído com sucesso.");
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }
        return "redirect:/app/usuarios";
    }

    private void carregarCidades(Model model) {
        List<Cidade> cidades = cidadeService.buscarTodos(null);
        model.addAttribute("cidades", cidades);
        model.addAttribute("semCidades", cidades.isEmpty());
    }

    private void configurarModoCriacao(Model model) {
        model.addAttribute("modoEdicao", false);
        model.addAttribute("usuarioId", null);
    }

    private void configurarModoEdicao(Model model, Long id) {
        model.addAttribute("modoEdicao", true);
        model.addAttribute("usuarioId", id);
    }
}
