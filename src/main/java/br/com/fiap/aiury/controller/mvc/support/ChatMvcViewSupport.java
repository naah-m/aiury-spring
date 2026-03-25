package br.com.fiap.aiury.controller.mvc.support;

import br.com.fiap.aiury.dto.web.ChatDetailView;
import br.com.fiap.aiury.dto.web.ChatMensagemItemView;
import br.com.fiap.aiury.dto.web.ChatStatusOptionView;
import br.com.fiap.aiury.dto.web.ChatStatusStepView;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.mappers.web.ChatWebMapper;
import br.com.fiap.aiury.services.AjudanteService;
import br.com.fiap.aiury.services.MensagemService;
import br.com.fiap.aiury.services.UsuarioService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class ChatMvcViewSupport {

    private static final String STATUS_NAO_INFORMADO = "Status nao informado";

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

    private final UsuarioService usuarioService;
    private final AjudanteService ajudanteService;
    private final MensagemService mensagemService;
    private final ChatWebMapper chatWebMapper;

    public ChatMvcViewSupport(UsuarioService usuarioService,
                              AjudanteService ajudanteService,
                              MensagemService mensagemService,
                              ChatWebMapper chatWebMapper) {
        this.usuarioService = usuarioService;
        this.ajudanteService = ajudanteService;
        this.mensagemService = mensagemService;
        this.chatWebMapper = chatWebMapper;
    }

    public void adicionarDadosListagem(Model model,
                                       Long usuarioId,
                                       Long ajudanteId,
                                       ChatStatus status) {
        model.addAttribute("filtroUsuarioId", usuarioId);
        model.addAttribute("filtroAjudanteId", ajudanteId);
        model.addAttribute("filtroStatus", status);
        model.addAttribute("temFiltrosAtivos", usuarioId != null || ajudanteId != null || status != null);
        model.addAttribute("usuarios", usuarioService.buscarTodos(null));
        model.addAttribute("ajudantes", ajudanteService.buscarTodos(null));
        model.addAttribute("statusOptions", STATUS_OPTIONS);
    }

    public void carregarDadosFormulario(Model model) {
        model.addAttribute("usuarios", usuarioService.buscarTodos(null));
        model.addAttribute("ajudantes", ajudanteService.buscarTodos(null));
        model.addAttribute("statusOptions", STATUS_OPTIONS);
    }

    public void prepararTelaDetalhes(Model model, Chat chat) {
        ChatDetailView chatDetailView = chatWebMapper.toDetailView(chat);
        List<ChatMensagemItemView> mensagens = buscarMensagensDoChat(chat.getId());

        model.addAttribute("chat", chatDetailView);
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        model.addAttribute("statusTimeline", montarLinhaDoTempo(chat.getStatus()));
        model.addAttribute("chatEncerrado", isFinalizado(chat.getStatus()));
        model.addAttribute("totalMensagens", mensagens.size());
    }

    public void prepararTelaConversa(Model model, Chat chat) {
        List<ChatMensagemItemView> mensagens = buscarMensagensDoChat(chat.getId());

        model.addAttribute("chat", chatWebMapper.toDetailView(chat));
        model.addAttribute("mensagens", mensagens);
        model.addAttribute("chatEncerrado", isFinalizado(chat.getStatus()));
        model.addAttribute("totalMensagens", mensagens.size());
    }

    public boolean isFinalizado(ChatStatus status) {
        return status == ChatStatus.FINALIZADO_USUARIO
                || status == ChatStatus.FINALIZADO_AJUDANTE
                || status == ChatStatus.FINALIZADO_SISTEMA;
    }

    public String resolverRotuloStatus(ChatStatus status) {
        if (status == null) {
            return STATUS_NAO_INFORMADO;
        }
        return STATUS_LABELS.getOrDefault(status, STATUS_NAO_INFORMADO);
    }

    public String resolverClasseStatus(ChatStatus status) {
        if (status == null) {
            return "status-desconhecido";
        }
        return "status-" + status.name().toLowerCase(Locale.ROOT).replace('_', '-');
    }

    private List<ChatMensagemItemView> buscarMensagensDoChat(Long chatId) {
        return mensagemService.buscarTodos(chatId, null).stream()
                .map(chatWebMapper::toMensagemItem)
                .toList();
    }

    private List<ChatStatusStepView> montarLinhaDoTempo(ChatStatus statusAtual) {
        if (statusAtual == null) {
            return List.of(
                    new ChatStatusStepView("Chat iniciado", false, false),
                    new ChatStatusStepView("Atendimento em andamento", false, false),
                    new ChatStatusStepView("Aguardando encerramento", false, false)
            );
        }

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
}
