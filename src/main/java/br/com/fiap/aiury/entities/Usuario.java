package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

/**
 * Entidade que representa a pessoa usuaria da plataforma.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "TB_USUARIO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TB_USUARIO_NR_CEL", columnNames = "NR_CELULAR")
        }
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NM_USUARIO_REAL", nullable = false, length = 100)
    private String nomeReal;

    @Column(name = "NM_USUARIO_ANONIMO", length = 50)
    private String nomeAnonimo;

    @Column(name = "DT_NASCIMENTO", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "NR_CELULAR", length = 20)
    private String celular;

    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_CIDADE",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TB_USUARIO_ID_CID")
    )
    private Cidade cidade;
}
