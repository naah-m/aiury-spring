package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {

    Optional<AdminAccount> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}
