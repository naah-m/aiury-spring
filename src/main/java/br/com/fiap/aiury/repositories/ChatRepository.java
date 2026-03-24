package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para operacoes de persistencia de {@link Chat}.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
