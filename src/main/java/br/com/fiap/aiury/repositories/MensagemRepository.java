package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acesso a dados para {@link Mensagem}.
 */
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}
