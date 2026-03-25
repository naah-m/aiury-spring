package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.dto.web.ChatDetailView;
import br.com.fiap.aiury.dto.web.ChatListItemView;
import br.com.fiap.aiury.dto.web.ChatMensagemItemView;
import br.com.fiap.aiury.dto.web.ChatStatusOptionView;
import br.com.fiap.aiury.dto.web.ChatStatusStepView;
import br.com.fiap.aiury.dto.web.ChatStatusWebForm;
import br.com.fiap.aiury.dto.web.ChatWebForm;
import br.com.fiap.aiury.dto.web.MensagemWebForm;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.web.ChatWebMapper;
import br.com.fiap.aiury.services.AjudanteService;
import br.com.fiap.aiury.services.ChatService;
import br.com.fiap.aiury.services.MensagemService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app/chats")
public class ChatMvcController {

    private static final List<ChatStatusOptionView> STATUS_OPTIONS = List.of(
            new ChatStatusOptionView(ChatStatus.INICIADO, "Iniciado"),
            new ChatStatusOptionView(ChatStatus.EM_ANDAMENTO, "Em andamento"),
            new ChatStatusOptionView(ChatStatus.FINALIZADO_USUARIO, "Finalizado pelo usuario"),
            new ChatStatusOptionView(ChatStatus.FINALIZADO_AJUDANTE, "Finalizado pelo ajudante"),
            new ChatStatusOptionView(ChatStatus.FINALIZADO_SISTEMA, "Finalizado pelo sistema")
    );

    private static final Map<ChatStatus, String> STATUS_LABELS = Map.of(
            ChatStatus.INICIADO, "Iniciado",
            ChatStatus.EM_ANDAMENTO, "Em andamento",
            ChatStatus.FINALIZADO_USUARIO, "Finalizado pelo usuario",
            ChatStatus.FINALIZADO_AJUDANTE, "Finalizado pelo ajudante",
            ChatStatus.FINALIZADO_SISTEMA, "Finalizado pelo sistema"
    );

    private final ChatService chatService;
    private final UsuarioService usuarioService;
    private final AjudanteService ajudanteService;
    private final MensagemService mensagemService;
    private final ChatWebMapper chatWebMapper;

    public ChatMvcController(ChatService chatService,
                             UsuarioService usuarioService,
                             AjudanteService ajudanteService,
                             MensagemService mensagemService,
                             ChatWebMapper chatWebMapper) {
        this.chatService = chatService;
        this.usuarioService = usuarioService;
        this.ajudanteService = ajudanteService;
        this.mensagemService = mensagemService;
        this.chatWebMapper = chatWebMapper;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long usuarioId,
                         @RequestParam(required = false) Long ajudanteId,
                         @RequestParam(required = false) ChatStatus status,
                         Model model) {
        List<ChatListItemView> chatViews = chatService.buscarTodos(usuarioId, ajudanteId, status).stream()
                .map(chatWebMapper::toListItem)
                .toList();

        model.addAttribute("chats", chatViews);
        model.addAttribute("filtroUsuarioId", usuarioId);
        model.addAttribute("filtroAjudanteId", ajudanteId);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("usuarios", usuarioService.buscarTodos(null));
        model.addAttribute("ajudantes", ajudanteService.buscarTodos(null));
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        model.addAttribute("statusLabels", STATUS_LABELS);
        return "app/chats/list";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        if (!model.containsAttribute("chatForm")) {
            ChatWebForm form = new ChatWebForm();
            form.setDataInicio(LocalDateTime.now().withSecond(0).withNano(0));
            form.setStatus(ChatStatus.INICIADO);
            model.addAttribute("chatForm", form);
        }

        carregarDadosFormulario(model);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        return "app/chats/form";
    }

    @PostMapping
    public String abrirNovoChat(@Valid @ModelAttribute("chatForm") ChatWebForm chatForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            carregarDadosFormulario(model);
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            return "app/chats/form";
        }

        try {
            Chat chat = chatService.criarChat(chatWebMapper.toRequestDto(chatForm));
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Chat aberto com sucesso.");
            return "redirect:/app/chats/" + chat.getId();
        } catch (NotFoundException | IllegalArgumentException ex) {
            carregarDadosFormulario(model);
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/form";
        }
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        Chat chat = chatService.buscarPorId(id);
        prepararTelaDetalhes(model, chat);

        if (!model.containsAttribute("statusForm")) {
            ChatStatusWebForm statusForm = new ChatStatusWebForm();
            statusForm.setStatus(chat.getStatus());
            statusForm.setDataFim(chat.getDataFim());
            model.addAttribute("statusForm", statusForm);
        }

        return "app/chats/detail";
    }

    @GetMapping("/{id}/conversa")
    public String conversar(@PathVariable Long id, Model model) {
        Chat chat = chatService.buscarPorId(id);
        prepararTelaConversa(model, chat);

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
            prepararTelaConversa(model, chat);
            return "app/chats/conversation";
        }

        if (isFinalizado(chat.getStatus())) {
            prepararTelaConversa(model, chat);
            model.addAttribute("mensagemErro", "Nao e possivel enviar mensagens em chats finalizados.");
            return "app/chats/conversation";
        }

        try {
            mensagemService.criarMensagem(
                    chatWebMapper.toMensagemRequest(
                            chat,
                            mensagemForm,
                            LocalDateTime.now().withSecond(0).withNano(0)
                    )
            );
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Mensagem enviada com sucesso.");
            return "redirect:/app/chats/" + id + "/conversa";
        } catch (NotFoundException | IllegalArgumentException ex) {
            prepararTelaConversa(model, chat);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/conversation";
        }
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @Valid @ModelAttribute("statusForm") ChatStatusWebForm statusForm,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Chat chat = chatService.buscarPorId(id);

        if (bindingResult.hasErrors()) {
            prepararTelaDetalhes(model, chat);
            return "app/chats/detail";
        }

        try {
            chatService.atualizarChat(id, chatWebMapper.toStatusUpdateRequest(chat, statusForm));
            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Status atualizado para " + resolverRotuloStatus(statusForm.getStatus()) + "."
            );
            return "redirect:/app/chats/" + id;
        } catch (NotFoundException | IllegalArgumentException ex) {
            prepararTelaDetalhes(model, chat);
            model.addAttribute("mensagemErro", ex.getMessage());
            return "app/chats/detail";
        }
    }

    private void carregarDadosFormulario(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos(null));
        model.addAttribute("ajudantes", ajudanteService.buscarTodos(null));
    }

    private void prepararTelaDetalhes(Model model, Chat chat) {
        ChatDetailView chatDetailView = chatWebMapper.toDetailView(chat);
        List<ChatMensagemItemView> mensagens = buscarMensagensDoChat(chat.getId());

        model.addAttribute("chat", chatDetailView);
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        model.addAttribute("statusLabels", STATUS_LABELS);
        model.addAttribute("statusTimeline", montarLinhaDoTempo(chat.getStatus()));
    }

    private void prepararTelaConversa(Model model, Chat chat) {
        List<ChatMensagemItemView> mensagens = buscarMensagensDoChat(chat.getId());

        model.addAttribute("chat", chatWebMapper.toDetailView(chat));
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("statusLabels", STATUS_LABELS);
        model.addAttribute("chatEncerrado", isFinalizado(chat.getStatus()));
        model.addAttribute("totalMensagens", mensagens.size());
    }

    private List<ChatMensagemItemView> buscarMensagensDoChat(Long chatId) {
        return mensagemService.buscarTodos(chatId, null).stream()
                .map(chatWebMapper::toMensagemItem)
                .toList();
    }

    private List<ChatStatusStepView> montarLinhaDoTempo(ChatStatus statusAtual) {
        boolean finalizado = isFinalizado(statusAtual);
        String etapaFinal = switch (statusAtual) {
            case FINALIZADO_USUARIO -> "Encerrado pelo usuario";
            case FINALIZADO_AJUDANTE -> "Encerrado pelo ajudante";
            case FINALIZADO_SISTEMA -> "Encerrado pelo sistema";
            default -> "Aguardando encerramento";
        };

        return List.of(
                new ChatStatusStepView("Chat iniciado", true, statusAtual == ChatStatus.INICIADO),
                new ChatStatusStepView(
                        "Atendimento em andamento",
                        statusAtual == ChatStatus.EM_ANDAMENTO || finalizado,
                        statusAtual == ChatStatus.EM_ANDAMENTO
                ),
                new ChatStatusStepView(etapaFinal, finalizado, finalizado)
        );
    }

    private boolean isFinalizado(ChatStatus status) {
        return status == ChatStatus.FINALIZADO_USUARIO
                || status == ChatStatus.FINALIZADO_AJUDANTE
                || status == ChatStatus.FINALIZADO_SISTEMA;
    }

    private String resolverRotuloStatus(ChatStatus status) {
        return STATUS_LABELS.getOrDefault(status, "status nao informado");
    }
}
