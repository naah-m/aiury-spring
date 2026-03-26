package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para operacoes de persistencia de {@link Chat}.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {

    @Override
    @EntityGraph(attributePaths = {"usuario", "usuario.cidade", "usuario.cidade.estado", "ajudante"})
    Optional<Chat> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"usuario", "usuario.cidade", "usuario.cidade.estado", "ajudante"})
    List<Chat> findAll(Specification<Chat> spec, Sort sort);

    Optional<Chat> findFirstByUsuario_IdAndAjudante_IdAndDataInicioOrderByIdAsc(
            Long usuarioId,
            Long ajudanteId,
            LocalDateTime dataInicio
    );

    Optional<Chat> findFirstByUsuario_IdAndStatusInOrderByDataInicioDesc(Long usuarioId, Collection<ChatStatus> status);

    Optional<Chat> findFirstByUsuario_IdAndStatusInAndIdNotOrderByDataInicioDesc(
            Long usuarioId,
            Collection<ChatStatus> status,
            Long id
    );

    long deleteByUsuario_Id(Long usuarioId);

    long deleteByAjudante_Id(Long ajudanteId);

    long countByUsuario_Id(Long usuarioId);

    long countByAjudante_Id(Long ajudanteId);
}
