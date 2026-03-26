package br.com.fiap.aiury.security;

import br.com.fiap.aiury.entities.AdminAccount;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.repositories.AdminAccountRepository;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AiuryUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final AdminAccountRepository adminAccountRepository;

    public AiuryUserDetailsService(UsuarioRepository usuarioRepository,
                                   AjudanteRepository ajudanteRepository,
                                   AdminAccountRepository adminAccountRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.adminAccountRepository = adminAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String login = username == null ? "" : username.trim();
        if (!StringUtils.hasText(login)) {
            throw new UsernameNotFoundException("Credencial invalida.");
        }

        AdminAccount admin = adminAccountRepository.findByUsernameIgnoreCase(login).orElse(null);
        if (admin != null) {
            return AiuryUserPrincipal.admin(admin.getUsername(), admin.getSenha());
        }

        Usuario usuario = usuarioRepository.findByCelular(login).orElse(null);
        if (usuario != null) {
            return AiuryUserPrincipal.usuario(login, usuario.getSenha(), usuario.getId());
        }

        Ajudante ajudante = ajudanteRepository.findByLoginIgnoreCase(login).orElse(null);
        if (ajudante != null) {
            return AiuryUserPrincipal.ajudante(ajudante.getLogin(), ajudante.getSenha(), ajudante.getId());
        }

        throw new UsernameNotFoundException("Usuario nao encontrado para o login informado.");
    }
}
