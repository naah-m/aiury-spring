package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade de referencia geografica para representar cidades.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "TB_CIDADE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_TB_CIDADE_NM_ID_ESTADO",
                        columnNames = {"NM_CIDADE", "ID_ESTADO"}
                )
        }
)
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CIDADE")
    private Long id;

    @Column(name = "NM_CIDADE", nullable = false, length = 100)
    private String nomeCidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_ESTADO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TB_CIDADE_ID_EST")
    )
    private Estado estado;
}
