package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para persistencia de {@link Usuario}.
 *
 * Papel na arquitetura:
 * - fornece operacoes CRUD padrao via Spring Data;
 * - e utilizado pela camada de servico para consultas e escrita.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAllByOrderByNomeRealAsc();

    /**
     * Retorna usuarios filtrados por cidade.
     *
     * @param cidadeId identificador da cidade
     * @return lista de usuarios da cidade
     */
    List<Usuario> findByCidade_Id(Long cidadeId);

    boolean existsByCelular(String celular);

    boolean existsByCelularAndIdNot(String celular, Long id);
}
