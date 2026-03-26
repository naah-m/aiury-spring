package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import br.com.fiap.aiury.security.AiuryAuthenticatedUserService;
import br.com.fiap.aiury.security.AiuryUserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

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
                    0,
                    chatRepository.countByUsuario_Id(usuarioId),
                    mensagemRepository.countByChat_Usuario_Id(usuarioId)
            );
        }

        if (principal.isAjudante()) {
            Long ajudanteId = principal.getAjudanteId();
            return new DashboardSummaryView(
                    0,
                    1,
                    chatRepository.countByAjudante_Id(ajudanteId),
                    mensagemRepository.countByChat_Ajudante_Id(ajudanteId)
            );
        }

        return new DashboardSummaryView(
                usuarioRepository.count(),
                ajudanteRepository.count(),
                chatRepository.count(),
                mensagemRepository.count()
        );
    }
}
