package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "ORACLE_TEST_ENABLED", matches = "true")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveFiltrarUsuariosPorCidade() {
        Estado estado = new Estado();
        estado.setNomeEstado("Sao Paulo");
        estado.setUf("SP");
        estado = estadoRepository.save(estado);

        Cidade cidadeSaoPaulo = new Cidade();
        cidadeSaoPaulo.setNomeCidade("Sao Paulo");
        cidadeSaoPaulo.setEstado(estado);
        cidadeSaoPaulo = cidadeRepository.save(cidadeSaoPaulo);

        Cidade cidadeSantos = new Cidade();
        cidadeSantos.setNomeCidade("Santos");
        cidadeSantos.setEstado(estado);
        cidadeSantos = cidadeRepository.save(cidadeSantos);

        Usuario usuario1 = new Usuario();
        usuario1.setNomeReal("Maria Silva");
        usuario1.setNomeAnonimo("LuzInterior");
        usuario1.setDataNascimento(LocalDate.of(1998, 8, 15));
        usuario1.setCelular("11999998888");
        usuario1.setSenha("segredo123");
        usuario1.setCidade(cidadeSaoPaulo);

        Usuario usuario2 = new Usuario();
        usuario2.setNomeReal("Joao Souza");
        usuario2.setNomeAnonimo("Foco");
        usuario2.setDataNascimento(LocalDate.of(1995, 3, 21));
        usuario2.setCelular("11988887777");
        usuario2.setSenha("segredo123");
        usuario2.setCidade(cidadeSantos);

        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        List<Usuario> resultado = usuarioRepository.findByCidade_Id(cidadeSaoPaulo.getId());

        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getNomeReal()).isEqualTo("Maria Silva");
    }
}
