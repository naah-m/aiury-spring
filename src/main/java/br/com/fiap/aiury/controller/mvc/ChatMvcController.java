package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.controller.mvc.support.ChatMvcViewSupport;
import br.com.fiap.aiury.dto.web.ChatListItemView;
import br.com.fiap.aiury.dto.web.ChatStatusWebForm;
import br.com.fiap.aiury.dto.web.ChatWebForm;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.web.ChatWebMapper;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.services.ChatService;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/app/chats")
public class ChatMvcController {

    private final ChatService chatService;
    private final ChatWebMapper chatWebMapper;
    private final ChatMvcViewSupport chatMvcViewSupport;
    private final AiuryAuthenticatedUserService authenticatedUserService;

    public ChatMvcController(ChatService chatService,
                             ChatWebMapper chatWebMapper,
                             ChatMvcViewSupport chatMvcViewSupport,
                             AiuryAuthenticatedUserService authenticatedUserService) {
        this.chatService = chatService;
        this.chatWebMapper = chatWebMapper;
        this.chatMvcViewSupport = chatMvcViewSupport;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long usuarioId,
                         @RequestParam(required = false) Long ajudanteId,
                         @RequestParam(required = false) ChatStatus status,
                         Model model) {
        Long usuarioIdEfetivo = usuarioId;
        Long ajudanteIdEfetivo = ajudanteId;
        if (authenticatedUserService.isUsuario()) {
            usuarioIdEfetivo = authenticatedUserService.getUsuarioIdOrNull();
            ajudanteIdEfetivo = null;
        } else if (authenticatedUserService.isAjudante()) {
            ajudanteIdEfetivo = authenticatedUserService.getAjudanteIdOrNull();
            usuarioIdEfetivo = null;
        }

        List<ChatListItemView> chatViews = chatService.buscarTodos(usuarioIdEfetivo, ajudanteIdEfetivo, status).stream()
                .map(chatWebMapper::toListItem)
                .toList();

        model.addAttribute("chats", chatViews);
        chatMvcViewSupport.adicionarDadosListagem(model, usuarioIdEfetivo, ajudanteIdEfetivo, status);
        return "app/chats/list";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        if (authenticatedUserService.isUsuario()) {
            Long usuarioId = authenticatedUserService.getUsuarioIdOrNull();
            if (usuarioId == null) {
                throw new AccessDeniedException("Perfil de usuário sem vínculo válido.");
            }
            model.addAttribute("usuarioNome", authenticatedUserService.getPrincipalOrThrow().getUsername());
            return "app/chats/open";
        }

        if (!model.containsAttribute("chatForm")) {
            ChatWebForm form = new ChatWebForm();
            form.setDataInicio(LocalDateTime.now().withSecond(0).withNano(0));
            form.setStatus(ChatStatus.INICIADO);
            model.addAttribute("chatForm", form);
        }

        chatMvcViewSupport.carregarDadosFormulario(model);
        return "app/chats/form";
    }

    @PostMapping("/abrir")
    public String abrirNovoChatParaUsuario(RedirectAttributes redirectAttributes) {
        if (!authenticatedUserService.isUsuario()) {
            throw new AccessDeniedException("Apenas usuários podem abrir novo chat.");
        }

        Long usuarioId = authenticatedUserService.getUsuarioIdOrNull();
        if (usuarioId == null) {
            throw new AccessDeniedException("Perfil de usuário sem vínculo válido.");
        }
        try {
            Chat chat = chatService.abrirChatParaUsuario(usuarioId);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Novo chat aberto com sucesso.");
            return "redirect:/app/chats/" + chat.getId() + "/conversa";
        } catch (ConflictException | NotFoundException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
            return "redirect:/app/chats/novo";
        }
    }

    @PostMapping
    public String abrirNovoChat(@Valid @ModelAttribute("chatForm") ChatWebForm chatForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            chatMvcViewSupport.carregarDadosFormulario(model);
            return "app/chats/form";
        }

        try {
            Chat chat = chatService.criarChat(chatWebMapper.toRequestDto(chatForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chat aberto com sucesso.");
            return "redirect:/app/chats/" + chat.getId();
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            chatMvcViewSupport.carregarDadosFormulario(model);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/form";
        }
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        Chat chat = chatService.buscarPorId(id);
        chatMvcViewSupport.prepararTelaDetalhes(model, chat);

        if (!model.containsAttribute("statusForm")) {
            ChatStatusWebForm statusForm = new ChatStatusWebForm();
            statusForm.setStatus(chat.getStatus());
            statusForm.setDataFim(chat.getDataFim());
            model.addAttribute("statusForm", statusForm);
        }

        return "app/chats/detail";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @Valid @ModelAttribute("statusForm") ChatStatusWebForm statusForm,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Chat chat = chatService.buscarPorId(id);

        if (bindingResult.hasErrors()) {
            chatMvcViewSupport.prepararTelaDetalhes(model, chat);
            return "app/chats/detail";
        }

        try {
            chatService.atualizarChat(id, chatWebMapper.toStatusUpdateRequest(chat, statusForm));
            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Status atualizado para " + chatMvcViewSupport.resolverRotuloStatus(statusForm.getStatus()) + "."
            );
            return "redirect:/app/chats/" + id;
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            chatMvcViewSupport.prepararTelaDetalhes(model, chat);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/detail";
        }
    }

    @PostMapping("/{id}/encerrar")
    public String encerrar(@PathVariable Long id,
                           @RequestParam(defaultValue = "detalhe") String retorno,
                           RedirectAttributes redirectAttributes) {
        try {
            Chat chatEncerrado = chatService.encerrarChat(id);
            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Chat encerrado como " + chatMvcViewSupport.resolverRotuloStatus(chatEncerrado.getStatus()) + "."
            );
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }

        return "conversa".equalsIgnoreCase(retorno)
                ? "redirect:/app/chats/" + id + "/conversa"
                : "redirect:/app/chats/" + id;
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            chatService.deletarChat(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chat excluído com sucesso.");
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }

        return "redirect:/app/chats";
    }
}
