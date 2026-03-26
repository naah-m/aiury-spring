package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "ORACLE_TEST_ENABLED", matches = "true")
class CidadeRepositoryTest {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveListarCidadesPorEstadoOrdenadasPorNome() {
        Estado sp = new Estado();
        sp.setNomeEstado("Sao Paulo");
        sp.setUf("SP");
        sp = estadoRepository.save(sp);

        Estado rj = new Estado();
        rj.setNomeEstado("Rio de Janeiro");
        rj.setUf("RJ");
        rj = estadoRepository.save(rj);

        Cidade campinas = new Cidade();
        campinas.setNomeCidade("Campinas");
        campinas.setEstado(sp);

        Cidade saoPaulo = new Cidade();
        saoPaulo.setNomeCidade("Sao Paulo");
        saoPaulo.setEstado(sp);

        Cidade niteroi = new Cidade();
        niteroi.setNomeCidade("Niteroi");
        niteroi.setEstado(rj);

        cidadeRepository.saveAll(List.of(saoPaulo, campinas, niteroi));

        List<Cidade> cidadesSp = cidadeRepository.findByEstado_IdOrderByNomeCidadeAsc(sp.getId());

        assertThat(cidadesSp).hasSize(2);
        assertThat(cidadesSp.get(0).getNomeCidade()).isEqualTo("Campinas");
        assertThat(cidadesSp.get(1).getNomeCidade()).isEqualTo("Sao Paulo");
    }
}
