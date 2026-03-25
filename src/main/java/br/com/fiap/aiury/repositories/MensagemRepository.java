package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de acesso a dados para {@link Mensagem}.
 */
@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    /**
     * Lista mensagens por chat.
     */
    List<Mensagem> findByChat_IdOrderByDataEnvioAsc(Long chatId);

    /**
     * Lista mensagens por remetente.
     */
    List<Mensagem> findByRemetente_IdOrderByDataEnvioAsc(Long remetenteId);

    /**
     * Lista mensagens por chat e remetente.
     */
    List<Mensagem> findByChat_IdAndRemetente_IdOrderByDataEnvioAsc(Long chatId, Long remetenteId);
}
