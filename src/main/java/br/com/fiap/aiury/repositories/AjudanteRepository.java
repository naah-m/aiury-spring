package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AjudanteRepository extends JpaRepository<Ajudante, Long> {
    List<Ajudante> findByIsDisponivel(boolean isDisponivel);
}
