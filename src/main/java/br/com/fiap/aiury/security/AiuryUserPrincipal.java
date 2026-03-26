package br.com.fiap.aiury.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AiuryUserPrincipal implements UserDetails {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USUARIO = "ROLE_USUARIO";
    public static final String ROLE_AJUDANTE = "ROLE_AJUDANTE";

    private final String username;
    private final String password;
    private final String role;
    private final Long usuarioId;
    private final Long ajudanteId;

    private AiuryUserPrincipal(String username, String password, String role, Long usuarioId, Long ajudanteId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.usuarioId = usuarioId;
        this.ajudanteId = ajudanteId;
    }

    public static AiuryUserPrincipal admin(String username, String password) {
        return new AiuryUserPrincipal(username, password, ROLE_ADMIN, null, null);
    }

    public static AiuryUserPrincipal usuario(String username, String password, Long usuarioId) {
        return new AiuryUserPrincipal(username, password, ROLE_USUARIO, usuarioId, null);
    }

    public static AiuryUserPrincipal ajudante(String username, String password, Long ajudanteId) {
        return new AiuryUserPrincipal(username, password, ROLE_AJUDANTE, null, ajudanteId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getAjudanteId() {
        return ajudanteId;
    }

    public boolean isAdmin() {
        return Objects.equals(ROLE_ADMIN, role);
    }

    public boolean isUsuario() {
        return Objects.equals(ROLE_USUARIO, role);
    }

    public boolean isAjudante() {
        return Objects.equals(ROLE_AJUDANTE, role);
    }
}
