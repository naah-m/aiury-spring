package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acesso a dados para {@link Cidade}.
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    @Override
    @EntityGraph(attributePaths = "estado")
    Optional<Cidade> findById(Long id);

    @EntityGraph(attributePaths = "estado")
    List<Cidade> findAllByOrderByNomeCidadeAsc();

    @EntityGraph(attributePaths = "estado")
    List<Cidade> findByEstado_IdOrderByNomeCidadeAsc(Long estadoId);

    boolean existsByNomeCidadeIgnoreCaseAndEstado_Id(String nomeCidade, Long estadoId);
}
