package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de persistencia para o catalogo de {@link Estado}.
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

    List<Estado> findAllByOrderByNomeEstadoAsc();

    Optional<Estado> findByUfIgnoreCase(String uf);

    boolean existsByNomeEstadoIgnoreCase(String nomeEstado);

    boolean existsByUfIgnoreCase(String uf);
}
