package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade de referencia para unidades federativas.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "TB_ESTADO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TB_ESTADO_NM_EST", columnNames = "NM_ESTADO"),
                @UniqueConstraint(name = "UK_TB_ESTADO_SG_EST", columnNames = "SG_ESTADO")
        }
)
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTADO")
    private Long id;

    @Column(name = "NM_ESTADO", nullable = false, length = 50)
    private String nomeEstado;

    @Column(name = "SG_ESTADO", nullable = false, length = 2)
    private String uf;

    @OneToMany(mappedBy = "estado")
    private List<Cidade> cidades = new ArrayList<>();
}
