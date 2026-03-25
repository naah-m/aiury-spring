package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
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
@Table(name = "tb_ajudante")
public class Ajudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "area_atuacao", length = 100, nullable = false)
    private String areaAtuacao;

    @Lob
    @Column(name = "motivacao")
    private String motivacao;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    @Column(name = "rating")
    private Double rating;
}
