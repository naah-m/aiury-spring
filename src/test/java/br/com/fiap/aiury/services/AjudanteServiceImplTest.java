package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AjudanteServiceImplTest {

    @Mock
    private AjudanteRepository ajudanteRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AjudanteMapper ajudanteMapper;

    @InjectMocks
    private AjudanteServiceImpl ajudanteService;

    @Test
    void deveLancarErroQuandoCriarSemSenha() {
        AjudanteRequestDTO dto = new AjudanteRequestDTO();
        dto.setLogin("ajudante.teste");
        dto.setAreaAtuacao("Escuta ativa");
        dto.setSenha(" ");

        assertThatThrownBy(() -> ajudanteService.criarAjudante(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("obrigatoria");
    }

    @Test
    void deveExcluirAjudanteComDependenciasQuandoAjudanteExiste() {
        Long ajudanteId = 5L;
        Ajudante ajudante = new Ajudante();
        ajudante.setId(ajudanteId);
        br.com.fiap.aiury.entities.Chat chat = new br.com.fiap.aiury.entities.Chat();
        chat.setId(200L);

        when(ajudanteRepository.findById(ajudanteId)).thenReturn(Optional.of(ajudante));
        when(chatRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(chat));

        ajudanteService.deletarAjudante(ajudanteId);

        verify(chatRepository).findAll(any(Specification.class), any(Sort.class));
        verify(chatRepository).delete(chat);
        verify(ajudanteRepository).delete(ajudante);
    }

    @Test
    void deveLancarNotFoundAoExcluirAjudanteInexistente() {
        Long ajudanteId = 999L;
        when(ajudanteRepository.findById(ajudanteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ajudanteService.deletarAjudante(ajudanteId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Ajudante nao encontrado");
    }

    @Test
    void deveAlterarSenhaQuandoSenhaAtualValida() {
        Ajudante ajudante = new Ajudante();
        ajudante.setId(5L);
        ajudante.setSenha("hash-atual");

        when(ajudanteRepository.findById(5L)).thenReturn(Optional.of(ajudante));
        when(passwordEncoder.matches("senhaAtual123", "hash-atual")).thenReturn(true);
        when(passwordEncoder.matches("novaSenha123", "hash-atual")).thenReturn(false);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("hash-nova");

        ajudanteService.alterarSenha(5L, "senhaAtual123", "novaSenha123");

        assertThat(ajudante.getSenha()).isEqualTo("hash-nova");
        verify(ajudanteRepository).save(ajudante);
    }

    @Test
    void deveLancarErroQuandoSenhaAtualNaoConfere() {
        Ajudante ajudante = new Ajudante();
        ajudante.setId(5L);
        ajudante.setSenha("hash-atual");

        when(ajudanteRepository.findById(5L)).thenReturn(Optional.of(ajudante));
        when(passwordEncoder.matches("senhaErrada", "hash-atual")).thenReturn(false);

        assertThatThrownBy(() -> ajudanteService.alterarSenha(5L, "senhaErrada", "novaSenha123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao confere");
    }
}
