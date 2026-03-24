package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de persistencia para {@link Ajudante}.
 *
 * Consulta customizada:
 * - {@link #findByDisponivel(boolean)} filtra ajudantes por status operacional.
 */
@Repository
public interface AjudanteRepository extends JpaRepository<Ajudante, Long> {

    /**
     * Retorna ajudantes filtrando pela disponibilidade atual.
     *
     * @param disponivel indicador de disponibilidade
     * @return lista de ajudantes com o status informado
     */
    List<Ajudante> findByDisponivel(boolean disponivel);
}
