package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private UsuarioModelAssembler usuarioModelAssembler;

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
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(model);

        mockMvc.perform(get("/api/usuarios/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeReal").value("Maria Silva"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
