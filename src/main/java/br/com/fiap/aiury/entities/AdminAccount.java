package br.com.fiap.aiury.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "TB_ADMIN_ACCOUNT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TB_ADMIN_NM_LOGIN", columnNames = "NM_LOGIN")
        }
)
public class AdminAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ADMIN_ACCOUNT")
    private Long id;

    @Column(name = "NM_LOGIN", nullable = false, length = 120)
    private String username;

    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @UpdateTimestamp
    @Column(name = "DH_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;
}
