package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade de referencia para unidades federativas.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_estado", nullable = false, length = 50, unique = true)
    private String nomeEstado;

    @Column(name = "uf", nullable = false, length = 2, unique = true)
    private String uf;
}
