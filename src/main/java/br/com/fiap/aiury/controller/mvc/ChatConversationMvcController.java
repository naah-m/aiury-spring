package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.controller.mvc.support.ChatMvcViewSupport;
import br.com.fiap.aiury.dto.web.MensagemWebForm;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.web.ChatWebMapper;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.services.ChatService;
import br.com.fiap.aiury.services.MensagemService;
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

import java.time.LocalDateTime;

@Controller
@RequestMapping("/app/chats")
public class ChatConversationMvcController {

    private final ChatService chatService;
    private final MensagemService mensagemService;
    private final ChatWebMapper chatWebMapper;
    private final ChatMvcViewSupport chatMvcViewSupport;
    private final AiuryAuthenticatedUserService authenticatedUserService;

    public ChatConversationMvcController(ChatService chatService,
                                         MensagemService mensagemService,
                                         ChatWebMapper chatWebMapper,
                                         ChatMvcViewSupport chatMvcViewSupport,
                                         AiuryAuthenticatedUserService authenticatedUserService) {
        this.chatService = chatService;
        this.mensagemService = mensagemService;
        this.chatWebMapper = chatWebMapper;
        this.chatMvcViewSupport = chatMvcViewSupport;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/{id}/conversa")
    public String conversar(@PathVariable Long id, Model model) {
        Chat chat = chatService.buscarPorId(id);
        chatMvcViewSupport.prepararTelaConversa(model, chat);

        if (!model.containsAttribute("mensagemForm")) {
            model.addAttribute("mensagemForm", new MensagemWebForm());
        }

        return "app/chats/conversation";
    }

    @PostMapping("/{id}/conversa/mensagens")
    public String enviarMensagem(@PathVariable Long id,
                                 @Valid @ModelAttribute("mensagemForm") MensagemWebForm mensagemForm,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Chat chat = chatService.buscarPorId(id);

        if (bindingResult.hasErrors()) {
            chatMvcViewSupport.prepararTelaConversa(model, chat);
            return "app/chats/conversation";
        }

        if (chatMvcViewSupport.isFinalizado(chat.getStatus())) {
            chatMvcViewSupport.prepararTelaConversa(model, chat);
            model.addAttribute("mensagemErro", "Nao e possivel enviar mensagens em chats finalizados.");
            return "app/chats/conversation";
        }

        try {
            Long remetenteUsuarioId = null;
            Long remetenteAjudanteId = null;
            if (authenticatedUserService.isUsuario()) {
                remetenteUsuarioId = authenticatedUserService.getUsuarioIdOrNull();
            } else if (authenticatedUserService.isAjudante()) {
                remetenteAjudanteId = authenticatedUserService.getAjudanteIdOrNull();
            } else {
                remetenteAjudanteId = chat.getAjudante() != null ? chat.getAjudante().getId() : null;
                if (remetenteAjudanteId == null && chat.getUsuario() != null) {
                    remetenteUsuarioId = chat.getUsuario().getId();
                }
            }

            mensagemService.criarMensagem(
                    chatWebMapper.toMensagemRequest(
                            chat,
                            mensagemForm,
                            LocalDateTime.now().withSecond(0).withNano(0),
                            remetenteUsuarioId,
                            remetenteAjudanteId
                    )
            );
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Mensagem enviada com sucesso.");
            return "redirect:/app/chats/" + id + "/conversa";
        } catch (NotFoundException | ConflictException | IllegalArgumentException ex) {
            chatMvcViewSupport.prepararTelaConversa(model, chat);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/conversation";
        }
    }
}
