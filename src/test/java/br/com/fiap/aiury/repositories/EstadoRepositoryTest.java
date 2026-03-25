package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Estado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EstadoRepositoryTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveBuscarEstadoPorUfIgnorandoCase() {
        Estado estado = new Estado();
        estado.setNomeEstado("Sao Paulo");
        estado.setUf("SP");
        estadoRepository.save(estado);

        Optional<Estado> encontrado = estadoRepository.findByUfIgnoreCase("sp");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNomeEstado()).isEqualTo("Sao Paulo");
    }
}
