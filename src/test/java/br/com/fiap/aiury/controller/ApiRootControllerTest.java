package br.com.fiap.aiury.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiRootController.class)
@ActiveProfiles("test")
class ApiRootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarEntrypointComLinksPrincipais() throws Exception {
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
}
