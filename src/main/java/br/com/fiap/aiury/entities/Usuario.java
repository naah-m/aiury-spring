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
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_real", nullable = false, length = 100)
    private String nomeReal;

    @Column(name = "nome_anonimo", length = 50)
    private String nomeAnonimo;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "celular", length = 20, unique = true)
    private String celular;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @CreationTimestamp
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;
}
