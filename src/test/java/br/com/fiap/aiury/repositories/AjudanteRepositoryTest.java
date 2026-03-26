package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Ajudante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AjudanteRepositoryTest {

    @Autowired
    private AjudanteRepository ajudanteRepository;

    @Test
    void deveFiltrarAjudantesPorDisponibilidade() {
        Ajudante disponivel = new Ajudante();
        disponivel.setAreaAtuacao("Escuta ativa");
        disponivel.setLogin("escuta.ativa");
        disponivel.setSenha("segredo123");
        disponivel.setMotivacao("Voluntario");
        disponivel.setDisponivel(true);
        disponivel.setRating(4.8);

        Ajudante indisponivel = new Ajudante();
        indisponivel.setAreaAtuacao("Psicologia");
        indisponivel.setLogin("psicologia");
        indisponivel.setSenha("segredo123");
        indisponivel.setMotivacao("Plantao");
        indisponivel.setDisponivel(false);
        indisponivel.setRating(4.5);

        ajudanteRepository.saveAll(List.of(disponivel, indisponivel));

        List<Ajudante> resultado = ajudanteRepository.findByDisponivel(true);
        assertThat(resultado).hasSize(1);
        assertThat(resultado.getFirst().getAreaAtuacao()).isEqualTo("Escuta ativa");
    }
}
