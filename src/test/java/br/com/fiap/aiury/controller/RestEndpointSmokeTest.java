package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.dto.ChatResponseDTO;
import br.com.fiap.aiury.dto.CidadeResponseDTO;
import br.com.fiap.aiury.dto.EstadoResponseDTO;
import br.com.fiap.aiury.dto.MensagemResponseDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.representation.AjudanteRepresentationBuilder;
import br.com.fiap.aiury.representation.ChatRepresentationBuilder;
import br.com.fiap.aiury.representation.CidadeRepresentationBuilder;
import br.com.fiap.aiury.representation.EstadoRepresentationBuilder;
import br.com.fiap.aiury.representation.MensagemRepresentationBuilder;
import br.com.fiap.aiury.representation.UsuarioRepresentationBuilder;
import br.com.fiap.aiury.services.AjudanteService;
import br.com.fiap.aiury.services.ChatService;
import br.com.fiap.aiury.services.CidadeService;
import br.com.fiap.aiury.services.EstadoService;
import br.com.fiap.aiury.services.MensagemService;
import br.com.fiap.aiury.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        ApiRootController.class,
        EstadoController.class,
        CidadeController.class,
        UsuarioController.class,
        AjudanteController.class,
        ChatController.class,
        MensagemController.class
})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RestEndpointSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoService estadoService;

    @MockitoBean
    private CidadeService cidadeService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private AjudanteService ajudanteService;

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private MensagemService mensagemService;

    @MockitoBean
    private EstadoRepresentationBuilder estadoRepresentationBuilder;

    @MockitoBean
    private CidadeRepresentationBuilder cidadeRepresentationBuilder;

    @MockitoBean
    private UsuarioRepresentationBuilder usuarioRepresentationBuilder;

    @MockitoBean
    private AjudanteRepresentationBuilder ajudanteRepresentationBuilder;

    @MockitoBean
    private ChatRepresentationBuilder chatRepresentationBuilder;

    @MockitoBean
    private MensagemRepresentationBuilder mensagemRepresentationBuilder;

    @Test
    void deveCobrirEndpointRaizDaApi() throws Exception {
        mockMvc.perform(get("/api").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.usuarios.href").exists())
                .andExpect(jsonPath("$._links.ajudantes.href").exists())
                .andExpect(jsonPath("$._links.chats.href").exists())
                .andExpect(jsonPath("$._links.mensagens.href").exists())
                .andExpect(jsonPath("$._links.cidades.href").exists())
                .andExpect(jsonPath("$._links.estados.href").exists());
    }

    @Test
    void deveCobrirCrudDeEstados() throws Exception {
        Estado estado = new Estado();
        estado.setId(1L);

        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(1L);
        dto.setNomeEstado("Sao Paulo");
        dto.setUf("SP");

        EntityModel<EstadoResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<EstadoResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(estadoService.criarEstado(any())).thenReturn(estado);
        when(estadoService.buscarPorId(1L)).thenReturn(estado);
        when(estadoService.buscarTodos("SP")).thenReturn(List.of(estado));
        when(estadoService.atualizarEstado(eq(1L), any())).thenReturn(estado);
        doNothing().when(estadoService).deletarEstado(1L);
        when(estadoRepresentationBuilder.toModel(estado)).thenReturn(model);
        when(estadoRepresentationBuilder.toCollection(List.of(estado), "SP")).thenReturn(collection);

        String body = """
                {
                  "nomeEstado": "Sao Paulo",
                  "uf": "SP"
                }
                """;

        mockMvc.perform(post("/api/estados").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/estados/1"))
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/estados").param("uf", "SP"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/estados/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(delete("/api/estados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCobrirCrudDeCidades() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(10L);

        CidadeResponseDTO dto = new CidadeResponseDTO();
        dto.setId(10L);
        dto.setNomeCidade("Sao Paulo");
        dto.setEstadoId(1L);
        dto.setEstadoNome("Sao Paulo");
        dto.setEstadoUf("SP");

        EntityModel<CidadeResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<CidadeResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(cidadeService.criarCidade(any())).thenReturn(cidade);
        when(cidadeService.buscarPorId(10L)).thenReturn(cidade);
        when(cidadeService.buscarTodos(1L)).thenReturn(List.of(cidade));
        when(cidadeService.atualizarCidade(eq(10L), any())).thenReturn(cidade);
        doNothing().when(cidadeService).deletarCidade(10L);
        when(cidadeRepresentationBuilder.toModel(cidade)).thenReturn(model);
        when(cidadeRepresentationBuilder.toCollection(List.of(cidade), 1L)).thenReturn(collection);

        String body = """
                {
                  "nomeCidade": "Sao Paulo",
                  "estadoId": 1
                }
                """;

        mockMvc.perform(post("/api/cidades").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/cidades/10"))
                .andExpect(jsonPath("$.id").value(10));

        mockMvc.perform(get("/api/cidades/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        mockMvc.perform(get("/api/cidades").param("estadoId", "1"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/cidades/10").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        mockMvc.perform(delete("/api/cidades/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCobrirCrudDeUsuarios() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(7L);

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(7L);
        dto.setNomeReal("Maria Silva");
        dto.setNomeAnonimo("LuzInterior");
        dto.setDataNascimento(LocalDate.of(1998, 8, 15));
        dto.setCelular("11999998888");
        dto.setDataCadastro(LocalDate.of(2026, 4, 9));
        dto.setCidadeId(10L);

        EntityModel<UsuarioResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<UsuarioResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(usuarioService.criarUsuario(any())).thenReturn(usuario);
        when(usuarioService.buscarPorId(7L)).thenReturn(usuario);
        when(usuarioService.buscarTodos(10L)).thenReturn(List.of(usuario));
        when(usuarioService.atualizarUsuario(eq(7L), any())).thenReturn(usuario);
        doNothing().when(usuarioService).deletarUsuario(7L);
        when(usuarioRepresentationBuilder.toModel(usuario)).thenReturn(model);
        when(usuarioRepresentationBuilder.toCollection(List.of(usuario), 10L)).thenReturn(collection);

        String body = """
                {
                  "nomeReal": "Maria Silva",
                  "nomeAnonimo": "LuzInterior",
                  "dataNascimento": "15/08/1998",
                  "celular": "11999998888",
                  "senha": "segredo123",
                  "cidadeId": 10
                }
                """;

        mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/usuarios/7"))
                .andExpect(jsonPath("$.id").value(7));

        mockMvc.perform(get("/api/usuarios/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));

        mockMvc.perform(get("/api/usuarios").param("cidadeId", "10"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/usuarios/7").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));

        mockMvc.perform(delete("/api/usuarios/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCobrirCrudDeAjudantes() throws Exception {
        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        AjudanteResponseDTO dto = new AjudanteResponseDTO();
        dto.setId(3L);
        dto.setAreaAtuacao("Escuta ativa");
        dto.setLogin("ajudante.escuta");
        dto.setMotivacao("Acolhimento");
        dto.setDisponivel(true);
        dto.setRating(4.9);

        EntityModel<AjudanteResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<AjudanteResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(ajudanteService.criarAjudante(any())).thenReturn(ajudante);
        when(ajudanteService.buscarPorId(3L)).thenReturn(ajudante);
        when(ajudanteService.buscarTodos(Boolean.TRUE)).thenReturn(List.of(ajudante));
        when(ajudanteService.atualizarAjudante(eq(3L), any())).thenReturn(ajudante);
        doNothing().when(ajudanteService).deletarAjudante(3L);
        when(ajudanteRepresentationBuilder.toModel(ajudante)).thenReturn(model);
        when(ajudanteRepresentationBuilder.toCollection(List.of(ajudante), Boolean.TRUE)).thenReturn(collection);

        String body = """
                {
                  "areaAtuacao": "Escuta ativa",
                  "login": "ajudante.escuta",
                  "senha": "ajudante123",
                  "motivacao": "Acolhimento",
                  "disponivel": true,
                  "rating": 4.9
                }
                """;

        mockMvc.perform(post("/api/ajudantes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/ajudantes/3"))
                .andExpect(jsonPath("$.id").value(3));

        mockMvc.perform(get("/api/ajudantes/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));

        mockMvc.perform(get("/api/ajudantes").param("disponivel", "true"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/ajudantes/3").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));

        mockMvc.perform(delete("/api/ajudantes/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCobrirCrudDeChats() throws Exception {
        Chat chat = new Chat();
        chat.setId(11L);

        ChatResponseDTO dto = new ChatResponseDTO();
        dto.setId(11L);
        dto.setUsuarioId(7L);
        dto.setAjudanteId(3L);
        dto.setDataInicio(LocalDateTime.of(2026, 3, 25, 14, 0));
        dto.setDataFim(LocalDateTime.of(2026, 3, 25, 15, 5));
        dto.setStatus(ChatStatus.EM_ANDAMENTO);

        EntityModel<ChatResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<ChatResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(chatService.criarChat(any())).thenReturn(chat);
        when(chatService.buscarPorId(11L)).thenReturn(chat);
        when(chatService.buscarTodos(7L, 3L, ChatStatus.EM_ANDAMENTO)).thenReturn(List.of(chat));
        when(chatService.atualizarChat(eq(11L), any())).thenReturn(chat);
        when(chatService.encerrarChat(11L)).thenReturn(chat);
        doNothing().when(chatService).deletarChat(11L);
        when(chatRepresentationBuilder.toModel(chat)).thenReturn(model);
        when(chatRepresentationBuilder.toCollection(List.of(chat), 7L, 3L, ChatStatus.EM_ANDAMENTO)).thenReturn(collection);

        String body = """
                {
                  "usuarioId": 7,
                  "ajudanteId": 3,
                  "dataInicio": "25/03/2026 14:00:00",
                  "dataFim": "25/03/2026 15:05:00",
                  "status": "EM_ANDAMENTO"
                }
                """;

        mockMvc.perform(post("/api/chats").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/chats/11"))
                .andExpect(jsonPath("$.id").value(11));

        mockMvc.perform(get("/api/chats/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11));

        mockMvc.perform(get("/api/chats")
                        .param("usuarioId", "7")
                        .param("ajudanteId", "3")
                        .param("status", "EM_ANDAMENTO"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/chats/11").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11));

        mockMvc.perform(post("/api/chats/11/encerrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11));

        mockMvc.perform(delete("/api/chats/11"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveCobrirCrudDeMensagens() throws Exception {
        Mensagem mensagem = new Mensagem();
        mensagem.setId(19L);

        MensagemResponseDTO dto = new MensagemResponseDTO();
        dto.setId(19L);
        dto.setChatId(11L);
        dto.setRemetenteId(7L);
        dto.setRemetenteTipo("USUARIO");
        dto.setTexto("Mensagem de teste");
        dto.setDataEnvio(LocalDateTime.of(2026, 3, 25, 14, 15));

        EntityModel<MensagemResponseDTO> model = EntityModel.of(dto);
        CollectionModel<EntityModel<MensagemResponseDTO>> collection = CollectionModel.of(List.of(model));

        when(mensagemService.criarMensagem(any())).thenReturn(mensagem);
        when(mensagemService.buscarPorId(19L)).thenReturn(mensagem);
        when(mensagemService.buscarTodos(11L, 7L)).thenReturn(List.of(mensagem));
        when(mensagemService.atualizarMensagem(eq(19L), any())).thenReturn(mensagem);
        doNothing().when(mensagemService).deletarMensagem(19L);
        when(mensagemRepresentationBuilder.toModel(mensagem)).thenReturn(model);
        when(mensagemRepresentationBuilder.toCollection(List.of(mensagem), 11L, 7L)).thenReturn(collection);

        String body = """
                {
                  "chatId": 11,
                  "remetenteId": 7,
                  "texto": "Mensagem de teste",
                  "dataEnvio": "25/03/2026 14:15:00"
                }
                """;

        mockMvc.perform(post("/api/mensagens").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/mensagens/19"))
                .andExpect(jsonPath("$.id").value(19));

        mockMvc.perform(get("/api/mensagens/19"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(19));

        mockMvc.perform(get("/api/mensagens")
                        .param("chatId", "11")
                        .param("remetenteId", "7"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/mensagens/19").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(19));

        mockMvc.perform(delete("/api/mensagens/19"))
                .andExpect(status().isNoContent());
    }
}
