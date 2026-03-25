package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;

    public DashboardServiceImpl(UsuarioRepository usuarioRepository,
                                AjudanteRepository ajudanteRepository,
                                ChatRepository chatRepository,
                                MensagemRepository mensagemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
    }

    @Override
    public DashboardSummaryView obterResumo() {
        return new DashboardSummaryView(
                usuarioRepository.count(),
                ajudanteRepository.count(),
                chatRepository.count(),
                mensagemRepository.count()
        );
    }
}
