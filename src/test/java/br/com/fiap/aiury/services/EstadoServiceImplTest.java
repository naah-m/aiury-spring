package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.mappers.EstadoMapper;
import br.com.fiap.aiury.repositories.EstadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoServiceImplTest {

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private EstadoMapper estadoMapper;

    @InjectMocks
    private EstadoServiceImpl estadoService;

    @Test
    void deveLancarConflictQuandoUfJaExiste() {
        EstadoRequestDTO dto = new EstadoRequestDTO();
        dto.setNomeEstado("Sao Paulo");
        dto.setUf("SP");

        when(estadoRepository.existsByNomeEstadoIgnoreCase("Sao Paulo")).thenReturn(false);
        when(estadoRepository.existsByUfIgnoreCase("SP")).thenReturn(true);

        assertThatThrownBy(() -> estadoService.criarEstado(dto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("UF");
    }

    @Test
    void deveLancarConflictQuandoNomeJaExiste() {
        EstadoRequestDTO dto = new EstadoRequestDTO();
        dto.setNomeEstado("Sao Paulo");
        dto.setUf("SP");

        when(estadoRepository.existsByNomeEstadoIgnoreCase("Sao Paulo")).thenReturn(true);

        assertThatThrownBy(() -> estadoService.criarEstado(dto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("nome");
    }
}
