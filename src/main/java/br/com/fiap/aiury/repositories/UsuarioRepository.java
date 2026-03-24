package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para persistencia de {@link Usuario}.
 *
 * Papel na arquitetura:
 * - fornece operacoes CRUD padrao via Spring Data;
 * - e utilizado pela camada de servico para consultas e escrita.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
