package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio JPA para operacoes de persistencia de {@link Chat}.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {

    Optional<Chat> findFirstByUsuario_IdAndAjudante_IdAndDataInicioOrderByIdAsc(
            Long usuarioId,
            Long ajudanteId,
            LocalDateTime dataInicio
    );
}
