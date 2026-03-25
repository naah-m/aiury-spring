package br.com.fiap.aiury.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    @WithMockUser(username = "atendente", roles = "ATENDENTE")
    void devePermitirAtendenteNoDashboardEListaDeUsuarios() throws Exception {
        mockMvc.perform(get("/app"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/app/usuarios"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/app/chats"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/app/chats/novo"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/app/chats/1/conversa"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "atendente", roles = "ATENDENTE")
    void deveNegarCadastroDeUsuarioParaPerfilAtendente() throws Exception {
        mockMvc.perform(get("/app/usuarios/novo"))
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
                                .with(user("atendente").roles("ATENDENTE"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isForbidden());
    }
}
