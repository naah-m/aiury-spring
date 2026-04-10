package br.com.fiap.aiury.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.HttpStatusAccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;

@Configuration
public class SecurityConfig {

    private static final RequestMatcher API_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults().matcher("/api/**");

    private final RoleBasedAuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(RoleBasedAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/acesso-negado", "/error").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/app/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/app/ajudantes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/app/chats").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/app/chats/abrir").hasRole("USUARIO")
                        .requestMatchers("/app/chats/novo").hasAnyRole("ADMIN", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/app/chats/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/app/chats/*/encerrar").hasAnyRole("USUARIO", "AJUDANTE")
                        .requestMatchers(HttpMethod.POST, "/app/chats/*/excluir").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/app/chats/*/conversa/mensagens").hasAnyRole("USUARIO", "AJUDANTE")
                        .requestMatchers("/app/minha-conta/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers("/app/chats/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers("/app/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")

                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/ajudantes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/chats/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers(HttpMethod.POST, "/api/chats/*/encerrar").hasAnyRole("USUARIO", "AJUDANTE")
                        .requestMatchers(HttpMethod.POST, "/api/chats/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/chats/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/chats/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/mensagens/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers(HttpMethod.POST, "/api/mensagens/**").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers(HttpMethod.PUT, "/api/mensagens/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/mensagens/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/cidades/**", "/api/estados/**", "/api").hasAnyRole("ADMIN", "USUARIO", "AJUDANTE")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .defaultAccessDeniedHandlerFor(
                                new HttpStatusAccessDeniedHandler(HttpStatus.FORBIDDEN),
                                API_REQUEST_MATCHER
                        )
                        .accessDeniedPage("/acesso-negado")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
        entryPoints.put(API_REQUEST_MATCHER, new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        DelegatingAuthenticationEntryPoint authenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        authenticationEntryPoint.setDefaultEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        return authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
