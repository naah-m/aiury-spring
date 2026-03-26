package br.com.fiap.aiury.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRedirecionarParaLoginQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/app"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void devePermitirAjudanteNoDashboardEListaDeChats() throws Exception {
        mockMvc.perform(
                        get("/app")
                                .with(user(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 1L)))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/app/chats")
                                .with(user(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 1L)))
                )
                .andExpect(status().isOk());
    }

    @Test
    void devePermitirUsuarioNoFluxoDeAberturaDeNovoChat() throws Exception {
        mockMvc.perform(
                        get("/app/chats/novo")
                                .with(user(AiuryUserPrincipal.usuario("11999998888", "x", 1L)))
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarAberturaDeNovoChatParaPerfilAjudante() throws Exception {
        mockMvc.perform(
                        get("/app/chats/novo")
                                .with(user(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 1L)))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void deveNegarCadastroDeUsuarioParaPerfilAjudante() throws Exception {
        mockMvc.perform(
                        get("/app/usuarios/novo")
                                .with(user(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 1L)))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void devePermitirMinhaContaParaTodosPerfisAutenticados() throws Exception {
        mockMvc.perform(
                        get("/app/minha-conta")
                                .with(user(AiuryUserPrincipal.usuario("11999998888", "x", 1L)))
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/app/minha-conta")
                                .with(user(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 1L)))
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/app/minha-conta")
                                .with(user(AiuryUserPrincipal.admin("admin", "x")))
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveNegarEnvioDeMensagemNoMvcParaAdmin() throws Exception {
        mockMvc.perform(
                        post("/app/chats/1/conversa/mensagens")
                                .with(csrf())
                                .with(user(AiuryUserPrincipal.admin("admin", "x")))
                                .param("texto", "Teste")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void deveRetornar401NaApiDeEscritaSemAutenticacao() throws Exception {
        mockMvc.perform(
                        post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar403NaApiDeEscritaParaPerfilSemPermissao() throws Exception {
        mockMvc.perform(
                        post("/api/usuarios")
                                .with(user(AiuryUserPrincipal.usuario("11999998888", "x", 10L)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isForbidden());
    }
}
