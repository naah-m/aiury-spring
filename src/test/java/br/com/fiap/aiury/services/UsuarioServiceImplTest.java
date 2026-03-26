package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MensagemRepository mensagemRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void deveLancarNotFoundQuandoCidadeNaoExisteAoCriarUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setCidadeId(999L);
        dto.setSenha("senha12345");

        when(cidadeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.criarUsuario(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cidade nao encontrada");
    }

    @Test
    void deveCriarUsuarioQuandoCidadeExiste() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNomeReal("Maria Silva");
        dto.setDataNascimento(LocalDate.of(1998, 8, 15));
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
        when(passwordEncoder.encode("segredo123")).thenReturn("senha-criptografada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario criado = usuarioService.criarUsuario(dto);
        assertThat(criado.getNomeReal()).isEqualTo("Maria Silva");
        assertThat(criado.getCidade().getId()).isEqualTo(1L);
    }

    @Test
    void deveExcluirUsuarioComDependenciasQuandoUsuarioExiste() {
        Long usuarioId = 10L;

        when(usuarioRepository.existsById(usuarioId)).thenReturn(true);

        usuarioService.deletarUsuario(usuarioId);

        verify(mensagemRepository).deleteByChat_Usuario_Id(usuarioId);
        verify(mensagemRepository).deleteByRemetente_Id(usuarioId);
        verify(chatRepository).deleteByUsuario_Id(usuarioId);
        verify(usuarioRepository).deleteById(usuarioId);
    }

    @Test
    void deveLancarNotFoundAoExcluirUsuarioInexistente() {
        Long usuarioId = 999L;
        when(usuarioRepository.existsById(usuarioId)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.deletarUsuario(usuarioId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Usuario nao encontrado");
    }

    @Test
    void deveAlterarSenhaQuandoSenhaAtualValida() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("hash-atual");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual123", "hash-atual")).thenReturn(true);
        when(passwordEncoder.matches("novaSenha123", "hash-atual")).thenReturn(false);
        when(passwordEncoder.encode("novaSenha123")).thenReturn("hash-nova");

        usuarioService.alterarSenha(1L, "senhaAtual123", "novaSenha123");

        assertThat(usuario.getSenha()).isEqualTo("hash-nova");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarErroQuandoSenhaAtualNaoConfere() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("hash-atual");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", "hash-atual")).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.alterarSenha(1L, "senhaErrada", "novaSenha123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nao confere");
    }
}
