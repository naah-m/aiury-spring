package br.com.fiap.aiury.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import static org.assertj.core.api.Assertions.assertThat;

class RoleBasedAuthenticationSuccessHandlerTest {

    @Test
    void deveRedirecionarParaDashboardQuandoNaoHaSavedRequest() throws Exception {
        RoleBasedAuthenticationSuccessHandler handler = new RoleBasedAuthenticationSuccessHandler();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "x");

        handler.onAuthenticationSuccess(request, response, authentication);

        assertThat(response.getRedirectedUrl()).isEqualTo("/app");
    }

    @Test
    void deveHonrarSavedRequestQuandoExistir() throws Exception {
        RoleBasedAuthenticationSuccessHandler handler = new RoleBasedAuthenticationSuccessHandler();

        MockHttpServletRequest originalRequest = new MockHttpServletRequest("GET", "/app/chats");
        MockHttpServletResponse originalResponse = new MockHttpServletResponse();
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.saveRequest(originalRequest, originalResponse);

        MockHttpServletRequest loginRequest = new MockHttpServletRequest("POST", "/login");
        loginRequest.setSession(originalRequest.getSession());
        MockHttpServletResponse loginResponse = new MockHttpServletResponse();
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "x");

        handler.onAuthenticationSuccess(loginRequest, loginResponse, authentication);

        assertThat(loginResponse.getRedirectedUrl()).contains("/app/chats");
    }
}
