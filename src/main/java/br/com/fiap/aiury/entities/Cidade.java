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
        name = "cidade",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cidade_nome_estado",
                        columnNames = {"nome_cidade", "id_estado"}
                )
        }
)
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_cidade", nullable = false, length = 100)
    private String nomeCidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
}
