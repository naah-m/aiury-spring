package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de acesso a dados para {@link Cidade}.
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    List<Cidade> findAllByOrderByNomeCidadeAsc();

    List<Cidade> findByEstado_IdOrderByNomeCidadeAsc(Long estadoId);

    boolean existsByNomeCidadeIgnoreCaseAndEstado_Id(String nomeCidade, Long estadoId);
}
