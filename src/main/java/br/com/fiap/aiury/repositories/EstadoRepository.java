package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de persistencia para o catalogo de {@link Estado}.
 */
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
