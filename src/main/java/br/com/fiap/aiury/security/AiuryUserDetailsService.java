package br.com.fiap.aiury.security;

import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AiuryUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final AiuryAdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;

    public AiuryUserDetailsService(UsuarioRepository usuarioRepository,
                                   AjudanteRepository ajudanteRepository,
                                   AiuryAdminProperties adminProperties,
                                   PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.adminProperties = adminProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String login = username == null ? "" : username.trim();
        if (!StringUtils.hasText(login)) {
            throw new UsernameNotFoundException("Credencial invalida.");
        }

        if (adminProperties.getUsername().equalsIgnoreCase(login)) {
            return AiuryUserPrincipal.admin(
                    adminProperties.getUsername(),
                    passwordEncoder.encode(adminProperties.getPassword())
            );
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
