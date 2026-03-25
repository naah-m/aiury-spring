package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void deveLancarNotFoundQuandoCidadeNaoExisteAoCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setCidadeId(999L);

        when(cidadeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.criarUsuario(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cidade nao encontrada");
    }

    @Test
    void deveCriarUsuarioQuandoCidadeExiste() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNomeReal("Maria Silva");
        dto.setDataNascimento("15-08-1998");
        dto.setSenha("segredo123");
        dto.setCidadeId(1L);

        Cidade cidade = new Cidade();
        cidade.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setNomeReal("Maria Silva");
        usuario.setDataNascimento(LocalDate.of(1998, 8, 15));
        usuario.setCidade(cidade);

        when(cidadeRepository.findById(1L)).thenReturn(Optional.of(cidade));
        when(usuarioMapper.toEntity(dto, cidade)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario criado = usuarioService.criarUsuario(dto);
        assertThat(criado.getNomeReal()).isEqualTo("Maria Silva");
        assertThat(criado.getCidade().getId()).isEqualTo(1L);
    }
}
