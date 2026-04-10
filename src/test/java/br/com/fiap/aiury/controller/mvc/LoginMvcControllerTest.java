package br.com.fiap.aiury.controller.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginMvcControllerTest {

    @Test
    void deveAceitarPostParaPaginaDeAcessoNegadoQuandoRequestForEncaminhadaPeloSecurity() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new LoginMvcController()).build();

        mockMvc.perform(post("/acesso-negado"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/access-denied"));
    }
}
