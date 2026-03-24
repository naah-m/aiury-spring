package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acesso a dados para {@link Cidade}.
 */
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
}
