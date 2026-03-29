package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DashboardServiceImplTest {

    private static final Set<ChatStatus> STATUS_ATIVOS = Set.of(ChatStatus.INICIADO, ChatStatus.EM_ANDAMENTO);
    private static final Set<ChatStatus> STATUS_FINALIZADOS = Set.of(
            ChatStatus.FINALIZADO_USUARIO,
            ChatStatus.FINALIZADO_AJUDANTE,
            ChatStatus.FINALIZADO_SISTEMA
    );

    @Test
    void deveMontarResumoParaAdminComTotaisGerais() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AjudanteRepository ajudanteRepository = mock(AjudanteRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        MensagemRepository mensagemRepository = mock(MensagemRepository.class);
        AiuryAuthenticatedUserService authenticatedUserService = mock(AiuryAuthenticatedUserService.class);

        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.admin("admin", "x"));
        when(usuarioRepository.count()).thenReturn(8L);
        when(ajudanteRepository.count()).thenReturn(5L);
        when(chatRepository.count()).thenReturn(13L);
        when(mensagemRepository.count()).thenReturn(34L);
        when(chatRepository.countByStatusIn(eq(STATUS_ATIVOS))).thenReturn(4L);
        when(chatRepository.countByStatusIn(eq(STATUS_FINALIZADOS))).thenReturn(9L);

        DashboardServiceImpl service = new DashboardServiceImpl(
                usuarioRepository,
                ajudanteRepository,
                chatRepository,
                mensagemRepository,
                authenticatedUserService
        );

        DashboardSummaryView summary = service.obterResumo();

        assertThat(summary.totalUsuarios()).isEqualTo(8L);
        assertThat(summary.totalAjudantes()).isEqualTo(5L);
        assertThat(summary.totalChats()).isEqualTo(13L);
        assertThat(summary.totalMensagens()).isEqualTo(34L);
        assertThat(summary.chatsAtivos()).isEqualTo(4L);
        assertThat(summary.chatsFinalizados()).isEqualTo(9L);
    }

    @Test
    void deveMontarResumoParaUsuarioComEscopoProprio() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AjudanteRepository ajudanteRepository = mock(AjudanteRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        MensagemRepository mensagemRepository = mock(MensagemRepository.class);
        AiuryAuthenticatedUserService authenticatedUserService = mock(AiuryAuthenticatedUserService.class);

        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.usuario("11999998888", "x", 11L));
        when(chatRepository.countDistinctAjudantesByUsuarioId(11L)).thenReturn(2L);
        when(chatRepository.countByUsuario_Id(11L)).thenReturn(6L);
        when(mensagemRepository.countByChat_Usuario_Id(11L)).thenReturn(15L);
        when(chatRepository.countByUsuario_IdAndStatusIn(eq(11L), eq(STATUS_ATIVOS))).thenReturn(1L);
        when(chatRepository.countByUsuario_IdAndStatusIn(eq(11L), eq(STATUS_FINALIZADOS))).thenReturn(5L);

        DashboardServiceImpl service = new DashboardServiceImpl(
                usuarioRepository,
                ajudanteRepository,
                chatRepository,
                mensagemRepository,
                authenticatedUserService
        );

        DashboardSummaryView summary = service.obterResumo();

        assertThat(summary.totalUsuarios()).isEqualTo(1L);
        assertThat(summary.totalAjudantes()).isEqualTo(2L);
        assertThat(summary.totalChats()).isEqualTo(6L);
        assertThat(summary.totalMensagens()).isEqualTo(15L);
        assertThat(summary.chatsAtivos()).isEqualTo(1L);
        assertThat(summary.chatsFinalizados()).isEqualTo(5L);
    }

    @Test
    void deveMontarResumoParaAjudanteComEscopoProprio() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AjudanteRepository ajudanteRepository = mock(AjudanteRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        MensagemRepository mensagemRepository = mock(MensagemRepository.class);
        AiuryAuthenticatedUserService authenticatedUserService = mock(AiuryAuthenticatedUserService.class);

        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", 7L));
        when(chatRepository.countDistinctUsuariosByAjudanteId(7L)).thenReturn(4L);
        when(chatRepository.countByAjudante_Id(7L)).thenReturn(9L);
        when(mensagemRepository.countByChat_Ajudante_Id(7L)).thenReturn(21L);
        when(chatRepository.countByAjudante_IdAndStatusIn(eq(7L), eq(STATUS_ATIVOS))).thenReturn(3L);
        when(chatRepository.countByAjudante_IdAndStatusIn(eq(7L), eq(STATUS_FINALIZADOS))).thenReturn(6L);

        DashboardServiceImpl service = new DashboardServiceImpl(
                usuarioRepository,
                ajudanteRepository,
                chatRepository,
                mensagemRepository,
                authenticatedUserService
        );

        DashboardSummaryView summary = service.obterResumo();

        assertThat(summary.totalUsuarios()).isEqualTo(4L);
        assertThat(summary.totalAjudantes()).isEqualTo(1L);
        assertThat(summary.totalChats()).isEqualTo(9L);
        assertThat(summary.totalMensagens()).isEqualTo(21L);
        assertThat(summary.chatsAtivos()).isEqualTo(3L);
        assertThat(summary.chatsFinalizados()).isEqualTo(6L);
    }

    @Test
    void deveNegarResumoQuandoUsuarioNaoTemVinculo() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AjudanteRepository ajudanteRepository = mock(AjudanteRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        MensagemRepository mensagemRepository = mock(MensagemRepository.class);
        AiuryAuthenticatedUserService authenticatedUserService = mock(AiuryAuthenticatedUserService.class);

        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.usuario("11999998888", "x", null));

        DashboardServiceImpl service = new DashboardServiceImpl(
                usuarioRepository,
                ajudanteRepository,
                chatRepository,
                mensagemRepository,
                authenticatedUserService
        );

        assertThatThrownBy(service::obterResumo)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("usuario autenticado sem vinculo");
    }

    @Test
    void deveNegarResumoQuandoAjudanteNaoTemVinculo() {
        UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
        AjudanteRepository ajudanteRepository = mock(AjudanteRepository.class);
        ChatRepository chatRepository = mock(ChatRepository.class);
        MensagemRepository mensagemRepository = mock(MensagemRepository.class);
        AiuryAuthenticatedUserService authenticatedUserService = mock(AiuryAuthenticatedUserService.class);

        when(authenticatedUserService.getPrincipalOrThrow()).thenReturn(AiuryUserPrincipal.ajudante("ajudante.escuta", "x", null));

        DashboardServiceImpl service = new DashboardServiceImpl(
                usuarioRepository,
                ajudanteRepository,
                chatRepository,
                mensagemRepository,
                authenticatedUserService
        );

        assertThatThrownBy(service::obterResumo)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("ajudante autenticado sem vinculo");
    }
}
