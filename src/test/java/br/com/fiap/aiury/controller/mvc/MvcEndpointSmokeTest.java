package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.configs.MvcExceptionHandler;
import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.dto.web.AjudanteListItemView;
import br.com.fiap.aiury.dto.web.AjudanteWebForm;
import br.com.fiap.aiury.dto.web.ChatListItemView;
import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.dto.web.UsuarioListItemView;
import br.com.fiap.aiury.dto.web.UsuarioWebForm;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.mappers.web.AjudanteWebMapper;
import br.com.fiap.aiury.mappers.web.ChatWebMapper;
import br.com.fiap.aiury.mappers.web.UsuarioWebMapper;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import br.com.fiap.aiury.services.AdminAccountService;
import br.com.fiap.aiury.services.AjudanteService;
import br.com.fiap.aiury.services.ChatService;
import br.com.fiap.aiury.services.CidadeService;
import br.com.fiap.aiury.services.DashboardService;
import br.com.fiap.aiury.services.MensagemService;
import br.com.fiap.aiury.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class MvcEndpointSmokeTest {

    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private AiuryAuthenticatedUserService authenticatedUserService;

    @Mock
    private AdminAccountService adminAccountService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AjudanteService ajudanteService;

    @Mock
    private CidadeService cidadeService;

    @Mock
    private ChatService chatService;

    @Mock
    private MensagemService mensagemService;

    @Mock
    private UsuarioWebMapper usuarioWebMapper;

    @Mock
    private AjudanteWebMapper ajudanteWebMapper;

    @Mock
    private ChatWebMapper chatWebMapper;

    @Mock
    private br.com.fiap.aiury.controller.mvc.support.ChatMvcViewSupport chatMvcViewSupport;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new HomeMvcController(dashboardService),
                        new LoginMvcController(),
                        new AccountMvcController(authenticatedUserService, adminAccountService, usuarioService, ajudanteService),
                        new UsuarioMvcController(usuarioService, cidadeService, usuarioWebMapper),
                        new AjudanteMvcController(ajudanteService, ajudanteWebMapper),
                        new ChatMvcController(chatService, chatWebMapper, chatMvcViewSupport, authenticatedUserService),
                        new ChatConversationMvcController(chatService, mensagemService, chatWebMapper, chatMvcViewSupport, authenticatedUserService)
                )
                .setControllerAdvice(new MvcExceptionHandler())
                .setConversionService(new DefaultFormattingConversionService())
                .setValidator(validator)
                .build();
    }

    @Test
    void deveCobrirEndpointsDeHomeELogin() throws Exception {
        when(dashboardService.obterResumo()).thenReturn(new DashboardSummaryView(1, 2, 3, 4, 5, 6));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"));

        mockMvc.perform(get("/app"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/dashboard"));

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));

        mockMvc.perform(get("/acesso-negado"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/access-denied"));

        mockMvc.perform(post("/acesso-negado"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/access-denied"));
    }

    @Test
    void deveCobrirEndpointsDeMinhaConta() throws Exception {
        AiuryUserPrincipal principal = AiuryUserPrincipal.admin("admin", "x");
        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(principal);
        doNothing().when(adminAccountService).alterarSenha(any(), any(), any());

        mockMvc.perform(get("/app/minha-conta"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/account/my-account"));

        mockMvc.perform(post("/app/minha-conta/senha")
                        .param("senhaAtual", "admin12345")
                        .param("novaSenha", "novaSenha123")
                        .param("confirmacaoNovaSenha", "novaSenha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/minha-conta"));
    }

    @Test
    void deveCobrirEndpointsDeUsuariosMvc() throws Exception {
        Usuario usuario = usuario(7L);
        Cidade cidade = cidade(10L);
        UsuarioWebForm form = new UsuarioWebForm();
        form.setNomeReal("Maria Silva");
        form.setNomeAnonimo("LuzInterior");
        form.setDataNascimento(LocalDate.of(1998, 8, 15));
        form.setCelular("11999998888");
        form.setCidadeId(10L);

        when(usuarioService.buscarTodos(null)).thenReturn(List.of(usuario));
        when(usuarioService.buscarPorId(7L)).thenReturn(usuario);
        when(usuarioService.criarUsuario(any())).thenReturn(usuario);
        when(usuarioService.atualizarUsuario(anyLong(), any())).thenReturn(usuario);
        doNothing().when(usuarioService).deletarUsuario(7L);
        when(cidadeService.buscarTodos(null)).thenReturn(List.of(cidade));
        when(usuarioWebMapper.toListItem(usuario)).thenReturn(
                new UsuarioListItemView(7L, "Maria Silva", "LuzInterior", "Sao Paulo", "SP", LocalDate.of(2026, 4, 9))
        );
        when(usuarioWebMapper.toForm(usuario)).thenReturn(form);
        when(usuarioWebMapper.toRequestDto(any())).thenReturn(new UsuarioRequestDTO());

        mockMvc.perform(get("/app/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/usuarios/list"));

        mockMvc.perform(get("/app/usuarios/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/usuarios/form"));

        mockMvc.perform(get("/app/usuarios/7/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/usuarios/form"));

        mockMvc.perform(post("/app/usuarios")
                        .param("nomeReal", "Maria Silva")
                        .param("nomeAnonimo", "LuzInterior")
                        .param("dataNascimento", "1998-08-15")
                        .param("celular", "11999998888")
                        .param("senha", "segredo123")
                        .param("cidadeId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/usuarios"));

        mockMvc.perform(post("/app/usuarios/7")
                        .param("nomeReal", "Maria Silva")
                        .param("nomeAnonimo", "LuzInterior")
                        .param("dataNascimento", "1998-08-15")
                        .param("celular", "11999998888")
                        .param("senha", "segredo123")
                        .param("cidadeId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/usuarios"));

        mockMvc.perform(post("/app/usuarios/7/excluir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/usuarios"));
    }

    @Test
    void deveCobrirEndpointsDeAjudantesMvc() throws Exception {
        Ajudante ajudante = ajudante(3L);
        AjudanteWebForm form = new AjudanteWebForm();
        form.setAreaAtuacao("Escuta ativa");
        form.setLogin("ajudante.escuta");
        form.setMotivacao("Acolhimento");
        form.setDisponivel(true);
        form.setRating(4.9);

        when(ajudanteService.buscarTodos(null)).thenReturn(List.of(ajudante));
        when(ajudanteService.buscarPorId(3L)).thenReturn(ajudante);
        when(ajudanteService.criarAjudante(any())).thenReturn(ajudante);
        when(ajudanteService.atualizarAjudante(anyLong(), any())).thenReturn(ajudante);
        doNothing().when(ajudanteService).deletarAjudante(3L);
        when(ajudanteWebMapper.toListItem(ajudante)).thenReturn(
                new AjudanteListItemView(3L, "Escuta ativa", "ajudante.escuta", true, 4.9)
        );
        when(ajudanteWebMapper.toForm(ajudante)).thenReturn(form);
        when(ajudanteWebMapper.toRequestDto(any())).thenReturn(new AjudanteRequestDTO());

        mockMvc.perform(get("/app/ajudantes"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/ajudantes/list"));

        mockMvc.perform(get("/app/ajudantes/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/ajudantes/form"));

        mockMvc.perform(get("/app/ajudantes/3/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/ajudantes/form"));

        mockMvc.perform(post("/app/ajudantes")
                        .param("areaAtuacao", "Escuta ativa")
                        .param("login", "ajudante.escuta")
                        .param("senha", "ajudante123")
                        .param("motivacao", "Acolhimento")
                        .param("disponivel", "true")
                        .param("rating", "4.9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/ajudantes"));

        mockMvc.perform(post("/app/ajudantes/3")
                        .param("areaAtuacao", "Escuta ativa")
                        .param("login", "ajudante.escuta")
                        .param("senha", "ajudante123")
                        .param("motivacao", "Acolhimento")
                        .param("disponivel", "true")
                        .param("rating", "4.9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/ajudantes"));

        mockMvc.perform(post("/app/ajudantes/3/excluir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/ajudantes"));
    }

    @Test
    void deveCobrirEndpointsDeChatsMvc() throws Exception {
        Chat chat = chat(11L, ChatStatus.EM_ANDAMENTO);

        when(chatService.buscarTodos(null, null, null)).thenReturn(List.of(chat));
        when(chatService.abrirChatParaUsuario(7L)).thenReturn(chat);
        when(chatService.criarChat(any())).thenReturn(chat);
        when(chatService.buscarPorId(11L)).thenReturn(chat);
        when(chatService.atualizarChat(anyLong(), any())).thenReturn(chat);
        when(chatService.encerrarChat(11L)).thenReturn(chat);
        doNothing().when(chatService).deletarChat(11L);
        when(chatWebMapper.toListItem(chat)).thenReturn(
                new ChatListItemView(11L, 7L, "Maria Silva", "LuzInterior", 3L, "Escuta ativa", true,
                        LocalDateTime.of(2026, 3, 25, 14, 0), null, ChatStatus.EM_ANDAMENTO)
        );
        when(chatWebMapper.toRequestDto(any())).thenReturn(new ChatRequestDTO());
        when(chatWebMapper.toStatusUpdateRequest(any(), any())).thenReturn(new ChatRequestDTO());
        doNothing().when(chatMvcViewSupport).adicionarDadosListagem(any(), any(), any(), any());
        doNothing().when(chatMvcViewSupport).carregarDadosFormulario(any());
        doNothing().when(chatMvcViewSupport).prepararTelaDetalhes(any(), any());
        when(chatMvcViewSupport.resolverRotuloStatus(ChatStatus.EM_ANDAMENTO)).thenReturn("Em andamento");

        when(authenticatedUserService.isUsuario()).thenReturn(false);
        when(authenticatedUserService.isAjudante()).thenReturn(false);

        mockMvc.perform(get("/app/chats"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/chats/list"));

        mockMvc.perform(get("/app/chats/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/chats/form"));

        when(authenticatedUserService.isUsuario()).thenReturn(true);
        when(authenticatedUserService.getUsuarioIdOrNull()).thenReturn(7L);
        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.usuario("11999998888", "x", 7L));

        mockMvc.perform(get("/app/chats/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/chats/open"));

        mockMvc.perform(post("/app/chats/abrir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11/conversa"));

        mockMvc.perform(post("/app/chats")
                        .param("usuarioId", "7")
                        .param("ajudanteId", "3")
                        .param("dataInicio", "2026-03-25T14:00")
                        .param("status", "INICIADO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11"));

        mockMvc.perform(get("/app/chats/11"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/chats/detail"));

        mockMvc.perform(post("/app/chats/11/status")
                        .param("status", "EM_ANDAMENTO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11"));

        mockMvc.perform(post("/app/chats/11/encerrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11"));

        mockMvc.perform(post("/app/chats/11/encerrar")
                        .param("retorno", "conversa"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11/conversa"));

        mockMvc.perform(post("/app/chats/11/excluir"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats"));
    }

    @Test
    void deveCobrirEndpointsDeConversaDoChatMvc() throws Exception {
        Chat chat = chat(11L, ChatStatus.EM_ANDAMENTO);

        when(chatService.buscarPorId(11L)).thenReturn(chat);
        doNothing().when(chatMvcViewSupport).prepararTelaConversa(any(), any());
        when(authenticatedUserService.isUsuario()).thenReturn(true);
        when(authenticatedUserService.getUsuarioIdOrNull()).thenReturn(7L);
        when(chatWebMapper.toMensagemRequest(any(), any(), any(), any(), any())).thenReturn(new MensagemRequestDTO());
        when(mensagemService.criarMensagem(any())).thenReturn(new br.com.fiap.aiury.entities.Mensagem());

        mockMvc.perform(get("/app/chats/11/conversa"))
                .andExpect(status().isOk())
                .andExpect(view().name("app/chats/conversation"));

        mockMvc.perform(post("/app/chats/11/conversa/mensagens")
                        .param("texto", "Mensagem de teste"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/app/chats/11/conversa"));
    }

    private Usuario usuario(Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        return usuario;
    }

    private Ajudante ajudante(Long id) {
        Ajudante ajudante = new Ajudante();
        ajudante.setId(id);
        return ajudante;
    }

    private Cidade cidade(Long id) {
        Cidade cidade = new Cidade();
        cidade.setId(id);
        cidade.setNomeCidade("Sao Paulo");
        return cidade;
    }

    private Chat chat(Long id, ChatStatus status) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setStatus(status);
        chat.setDataInicio(LocalDateTime.of(2026, 3, 25, 14, 0));
        return chat;
    }
}
