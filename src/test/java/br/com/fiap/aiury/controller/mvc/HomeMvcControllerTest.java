package br.com.fiap.aiury.controller.mvc;

import br.com.fiap.aiury.dto.web.DashboardSummaryView;
import br.com.fiap.aiury.services.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeMvcControllerTest {

    @Test
    void deveRenderizarHomePublicaQuandoNaoAutenticado() {
        DashboardService dashboardService = mock(DashboardService.class);
        HomeMvcController controller = new HomeMvcController(dashboardService);

        String view = controller.root(null);

        assertThat(view).isEqualTo("home/index");
    }

    @Test
    void deveRedirecionarParaDashboardQuandoAutenticado() {
        DashboardService dashboardService = mock(DashboardService.class);
        HomeMvcController controller = new HomeMvcController(dashboardService);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                "x",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String view = controller.root(authentication);

        assertThat(view).isEqualTo("redirect:/app");
    }

    @Test
    void deveManterHomePublicaParaTokenAnonimo() {
        DashboardService dashboardService = mock(DashboardService.class);
        HomeMvcController controller = new HomeMvcController(dashboardService);
        AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );

        String view = controller.root(authentication);

        assertThat(view).isEqualTo("home/index");
    }

    @Test
    void devePopularResumoNoDashboard() {
        DashboardService dashboardService = mock(DashboardService.class);
        when(dashboardService.obterResumo()).thenReturn(new DashboardSummaryView(1, 2, 3, 4, 5, 6));
        HomeMvcController controller = new HomeMvcController(dashboardService);
        ConcurrentModel model = new ConcurrentModel();

        String view = controller.dashboard(model);

        assertThat(view).isEqualTo("app/dashboard");
        assertThat(model.getAttribute("summary")).isNotNull();
        assertThat(model.getAttribute("generatedAtLabel")).isNotNull();
    }
}
