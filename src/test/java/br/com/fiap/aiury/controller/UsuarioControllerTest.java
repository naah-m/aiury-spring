package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.representation.UsuarioRepresentationBuilder;
import br.com.fiap.aiury.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioRepresentationBuilder usuarioRepresentationBuilder;

    @Test
    void deveRetornarUsuarioComHateoasNoGetById() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);
        dto.setNomeReal("Maria Silva");

        EntityModel<UsuarioResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(1L)).withSelfRel()
        );

        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);
        when(usuarioRepresentationBuilder.toModel(usuario)).thenReturn(model);

        mockMvc.perform(get("/api/usuarios/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeReal").value("Maria Silva"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void deveRetornar400QuandoPayloadDeUsuarioForInvalido() throws Exception {
        String bodyInvalido = """
                {
                  "nomeReal": "",
                  "dataNascimento": null,
                  "senha": "123",
                  "cidadeId": null
                }
                """;

        mockMvc.perform(
                        post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyInvalido)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.nomeReal").exists())
                .andExpect(jsonPath("$.validationErrors.senha").exists());
    }

    @Test
    void deveRetornar400QuandoFormatoDeDataForInvalido() throws Exception {
        String bodyDataInvalida = """
                {
                  "nomeReal": "Maria Silva",
                  "nomeAnonimo": "LuzInterior",
                  "dataNascimento": "1998-08-15",
                  "celular": "11999998888",
                  "senha": "segredo123",
                  "cidadeId": 1
                }
                """;

        mockMvc.perform(
                        post("/api/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyDataInvalida)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Formato de data invalido no corpo da requisicao."))
                .andExpect(jsonPath("$.validationErrors.dataNascimento").value("Formato de data invalido. Use dd/MM/yyyy."));
    }
}
