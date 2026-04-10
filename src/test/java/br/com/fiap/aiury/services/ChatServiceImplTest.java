package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.ChatMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AjudanteRepository ajudanteRepository;

    @Mock
    private AiuryAuthenticatedUserService authenticatedUserService;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    void deveExcluirChatComMensagensQuandoChatExiste() {
        Long chatId = 5L;
        Chat chat = new Chat();
        chat.setId(chatId);

        when(authenticatedUserService.isAdmin()).thenReturn(true);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        chatService.deletarChat(chatId);

        verify(chatRepository).delete(chat);
    }

    @Test
    void deveLancarNotFoundAoExcluirChatInexistente() {
        Long chatId = 999L;
        when(authenticatedUserService.isAdmin()).thenReturn(true);
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chatService.deletarChat(chatId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Chat nao encontrado");
    }

    @Test
    void deveBloquearCriacaoQuandoUsuarioJaPossuiChatAtivo() {
        ChatRequestDTO dto = new ChatRequestDTO();
        dto.setUsuarioId(1L);
        dto.setAjudanteId(3L);
        dto.setStatus(ChatStatus.INICIADO);
        dto.setDataInicio(LocalDateTime.now());

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        Chat ativo = new Chat();
        ativo.setId(99L);

        when(authenticatedUserService.isAdmin()).thenReturn(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ajudanteRepository.findById(3L)).thenReturn(Optional.of(ajudante));
        when(chatRepository.findFirstByUsuario_IdAndStatusInOrderByDataInicioDesc(any(), any())).thenReturn(Optional.of(ativo));

        assertThatThrownBy(() -> chatService.criarChat(dto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("chat ativo");
    }

    @Test
    void deveAbrirChatParaUsuarioQuandoNaoExisteAtivo() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);
        ajudante.setDisponivel(true);

        Chat salvo = new Chat();
        salvo.setId(10L);
        salvo.setUsuario(usuario);
        salvo.setAjudante(ajudante);
        salvo.setStatus(ChatStatus.INICIADO);

        when(authenticatedUserService.getPrincipalOrThrow())
                .thenReturn(AiuryUserPrincipal.usuario("11999998888", "x", 1L));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(chatRepository.findFirstByUsuario_IdAndStatusInOrderByDataInicioDesc(any(), any())).thenReturn(Optional.empty());
        when(ajudanteRepository.findFirstByDisponivelTrueOrderByRatingDescIdAsc()).thenReturn(Optional.of(ajudante));
        when(chatRepository.save(any(Chat.class))).thenReturn(salvo);

        Chat chat = chatService.abrirChatParaUsuario(1L);
        assertThat(chat.getId()).isEqualTo(10L);
        assertThat(chat.getStatus()).isEqualTo(ChatStatus.INICIADO);
    }

    @Test
    void deveEncerrarChatComoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        Chat chat = new Chat();
        chat.setId(10L);
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setStatus(ChatStatus.EM_ANDAMENTO);
        chat.setDataInicio(LocalDateTime.now().minusMinutes(5));

        when(chatRepository.findById(10L)).thenReturn(Optional.of(chat));
        when(authenticatedUserService.getPrincipalOrThrow())
                .thenReturn(AiuryUserPrincipal.usuario("11999998888", "x", 1L));
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chat encerrado = chatService.encerrarChat(10L);

        assertThat(encerrado.getStatus()).isEqualTo(ChatStatus.FINALIZADO_USUARIO);
        assertThat(encerrado.getDataFim()).isNotNull();
        verify(chatRepository).save(chat);
    }

    @Test
    void deveEncerrarChatComoAjudante() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        Chat chat = new Chat();
        chat.setId(10L);
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setStatus(ChatStatus.INICIADO);
        chat.setDataInicio(LocalDateTime.now().minusMinutes(2));

        when(chatRepository.findById(10L)).thenReturn(Optional.of(chat));
        when(authenticatedUserService.getPrincipalOrThrow())
                .thenReturn(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 3L));
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chat encerrado = chatService.encerrarChat(10L);

        assertThat(encerrado.getStatus()).isEqualTo(ChatStatus.FINALIZADO_AJUDANTE);
        assertThat(encerrado.getDataFim()).isNotNull();
        verify(chatRepository).save(chat);
    }

    @Test
    void deveBloquearEncerramentoPorAdministrador() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        Chat chat = new Chat();
        chat.setId(10L);
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setStatus(ChatStatus.EM_ANDAMENTO);
        chat.setDataInicio(LocalDateTime.now().minusMinutes(5));

        when(chatRepository.findById(10L)).thenReturn(Optional.of(chat));
        when(authenticatedUserService.getPrincipalOrThrow())
                .thenReturn(AiuryUserPrincipal.admin("admin", "x"));

        assertThatThrownBy(() -> chatService.encerrarChat(10L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Apenas usuario ou ajudante");
    }

    @Test
    void deveBloquearNovoEncerramentoDeChatJaFinalizado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Ajudante ajudante = new Ajudante();
        ajudante.setId(3L);

        Chat chat = new Chat();
        chat.setId(10L);
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setStatus(ChatStatus.FINALIZADO_AJUDANTE);
        chat.setDataInicio(LocalDateTime.now().minusMinutes(5));
        chat.setDataFim(LocalDateTime.now().minusMinutes(1));

        when(chatRepository.findById(10L)).thenReturn(Optional.of(chat));
        when(authenticatedUserService.getPrincipalOrThrow())
                .thenReturn(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 3L));

        assertThatThrownBy(() -> chatService.encerrarChat(10L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("ja esta finalizado");
    }
}
