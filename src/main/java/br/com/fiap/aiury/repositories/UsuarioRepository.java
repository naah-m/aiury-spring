package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para persistencia de {@link Usuario}.
 *
 * Papel na arquitetura:
 * - fornece operações CRUD padrao via Spring Data;
 * - e utilizado pela camada de servico para consultas e escrita.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Override
    @EntityGraph(attributePaths = {"cidade", "cidade.estado"})
    Optional<Usuario> findById(Long id);

    @EntityGraph(attributePaths = {"cidade", "cidade.estado"})
    List<Usuario> findAllByOrderByNomeRealAsc();

    /**
     * Retorna usuarios filtrados por cidade.
     *
     * @param cidadeId identificador da cidade
     * @return lista de usuarios da cidade
     */
    @EntityGraph(attributePaths = {"cidade", "cidade.estado"})
    List<Usuario> findByCidade_Id(Long cidadeId);

    Optional<Usuario> findByCelular(String celular);

    boolean existsByCelular(String celular);

    boolean existsByCelularAndIdNot(String celular, Long id);
}

