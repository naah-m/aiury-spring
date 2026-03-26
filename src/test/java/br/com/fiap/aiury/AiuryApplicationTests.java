package br.com.fiap.aiury;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "ORACLE_TEST_ENABLED", matches = "true")
class AiuryApplicationTests {

	@Test
	void contextLoads() {
	}

}
