package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Set<ChatStatus> STATUS_ATIVOS = EnumSet.of(ChatStatus.INICIADO, ChatStatus.EM_ANDAMENTO);
    private static final Set<ChatStatus> STATUS_FINALIZADOS = EnumSet.of(
            ChatStatus.FINALIZADO_USUARIO,
            ChatStatus.FINALIZADO_AJUDANTE,
            ChatStatus.FINALIZADO_SISTEMA
    );

    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;
    private final AiuryAuthenticatedUserService authenticatedUserService;

    public DashboardServiceImpl(UsuarioRepository usuarioRepository,
                                AjudanteRepository ajudanteRepository,
                                ChatRepository chatRepository,
                                MensagemRepository mensagemRepository,
                                AiuryAuthenticatedUserService authenticatedUserService) {
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    public DashboardSummaryView obterResumo() {
        AiuryUserPrincipal principal = authenticatedUserService.getPrincipalOrThrow();
        if (principal.isUsuario()) {
            Long usuarioId = principal.getUsuarioId();
            return new DashboardSummaryView(
                    1,
                    chatRepository.countDistinctAjudante_IdByUsuario_IdAndAjudante_IdIsNotNull(usuarioId),
                    chatRepository.countByUsuario_Id(usuarioId),
                    mensagemRepository.countByChat_Usuario_Id(usuarioId),
                    chatRepository.countByUsuario_IdAndStatusIn(usuarioId, STATUS_ATIVOS),
                    chatRepository.countByUsuario_IdAndStatusIn(usuarioId, STATUS_FINALIZADOS)
            );
        }

        if (principal.isAjudante()) {
            Long ajudanteId = principal.getAjudanteId();
            return new DashboardSummaryView(
                    chatRepository.countDistinctUsuario_IdByAjudante_IdAndUsuario_IdIsNotNull(ajudanteId),
                    1,
                    chatRepository.countByAjudante_Id(ajudanteId),
                    mensagemRepository.countByChat_Ajudante_Id(ajudanteId),
                    chatRepository.countByAjudante_IdAndStatusIn(ajudanteId, STATUS_ATIVOS),
                    chatRepository.countByAjudante_IdAndStatusIn(ajudanteId, STATUS_FINALIZADOS)
            );
        }

        return new DashboardSummaryView(
                usuarioRepository.count(),
                ajudanteRepository.count(),
                chatRepository.count(),
                mensagemRepository.count(),
                chatRepository.countByStatusIn(STATUS_ATIVOS),
                chatRepository.countByStatusIn(STATUS_FINALIZADOS)
        );
    }
}
