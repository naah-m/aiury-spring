package br.com.fiap.aiury.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa o perfil de ajudante.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "TB_AJUDANTE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TB_AJUDANTE_NM_LOGIN", columnNames = "NM_LOGIN")
        }
)
public class Ajudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AJUDANTE")
    private Long id;

    @Column(name = "NM_AREA_ATUACAO", length = 100, nullable = false)
    private String areaAtuacao;

    @Column(name = "NM_LOGIN", length = 60, nullable = false)
    private String login;

    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @Lob
    @Column(name = "DS_MOTIVACAO")
    private String motivacao;

    @Column(name = "FL_DISPONIVEL", nullable = false)
    private boolean disponivel;

    @Column(name = "NR_RATING")
    private Double rating;
}
