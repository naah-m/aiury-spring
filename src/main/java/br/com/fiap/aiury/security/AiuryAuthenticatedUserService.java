package br.com.fiap.aiury.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AiuryAuthenticatedUserService {

    public AiuryUserPrincipal getPrincipalOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        if (authentication.getPrincipal() instanceof AiuryUserPrincipal principal) {
            return principal;
        }

        if (hasAuthority(authentication, AiuryUserPrincipal.ROLE_ADMIN)) {
            return AiuryUserPrincipal.admin(authentication.getName(), "");
        }
        if (hasAuthority(authentication, AiuryUserPrincipal.ROLE_USUARIO)) {
            return AiuryUserPrincipal.usuario(authentication.getName(), "", null);
        }
        if (hasAuthority(authentication, AiuryUserPrincipal.ROLE_AJUDANTE)) {
            return AiuryUserPrincipal.ajudante(authentication.getName(), "", null);
        }

        throw new AccessDeniedException("Perfil autenticado sem role valida.");
    }

    public boolean isAdmin() {
        return getPrincipalOrThrow().isAdmin();
    }

    public boolean isUsuario() {
        return getPrincipalOrThrow().isUsuario();
    }

    public boolean isAjudante() {
        return getPrincipalOrThrow().isAjudante();
    }

    public Long getUsuarioIdOrNull() {
        return getPrincipalOrThrow().getUsuarioId();
    }

    public Long getAjudanteIdOrNull() {
        return getPrincipalOrThrow().getAjudanteId();
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> authority.equals(grantedAuthority.getAuthority()));
    }
}
