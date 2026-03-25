package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.EstadoRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HateoasRuntimeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @BeforeEach
    void limparBase() {
        usuarioRepository.deleteAll();
        cidadeRepository.deleteAll();
        estadoRepository.deleteAll();
    }

    @Test
    void deveListarCidadesComLinksSemErroDeLazyInitialization() throws Exception {
        Estado estado = criarEstado();
        Cidade cidade = criarCidade(estado);

        mockMvc.perform(get("/api/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cidadeResponseDTOList[0].id").value(cidade.getId()))
                .andExpect(jsonPath("$._embedded.cidadeResponseDTOList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.cidadeResponseDTOList[0]._links.estado.href").exists());
    }

    @Test
    void deveBuscarUsuarioPorIdComLinksSemErroDeLazyInitialization() throws Exception {
        Estado estado = criarEstado();
        Cidade cidade = criarCidade(estado);
        Usuario usuario = criarUsuario(cidade);

        mockMvc.perform(get("/api/usuarios/{id}", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuario.getId()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.cidade.href").exists())
                .andExpect(jsonPath("$._links.estado.href").exists());
    }

    private Estado criarEstado() {
        Estado estado = new Estado();
        estado.setNomeEstado("Parana");
        estado.setUf("PR");
        return estadoRepository.save(estado);
    }

    private Cidade criarCidade(Estado estado) {
        Cidade cidade = new Cidade();
        cidade.setNomeCidade("Curitiba");
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    private Usuario criarUsuario(Cidade cidade) {
        Usuario usuario = new Usuario();
        usuario.setNomeReal("Ana Souza");
        usuario.setNomeAnonimo("Aurora");
        usuario.setDataNascimento(LocalDate.of(1995, 1, 10));
        usuario.setCelular("41999998888");
        usuario.setSenha("segredo123");
        usuario.setCidade(cidade);
        return usuarioRepository.save(usuario);
    }
}

