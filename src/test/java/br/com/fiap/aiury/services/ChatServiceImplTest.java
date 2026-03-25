package br.com.fiap.aiury.services;

import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.ChatMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private MensagemRepository mensagemRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    void deveExcluirChatComMensagensQuandoChatExiste() {
        Long chatId = 5L;
        when(chatRepository.existsById(chatId)).thenReturn(true);

        chatService.deletarChat(chatId);

        verify(mensagemRepository).deleteByChat_Id(chatId);
        verify(chatRepository).deleteById(chatId);
    }

    @Test
    void deveLancarNotFoundAoExcluirChatInexistente() {
        Long chatId = 999L;
        when(chatRepository.existsById(chatId)).thenReturn(false);

        assertThatThrownBy(() -> chatService.deletarChat(chatId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Chat nao encontrado");
    }
}
