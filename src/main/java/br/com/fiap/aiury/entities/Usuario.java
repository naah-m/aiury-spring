package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_usuario")
    private Long id;

    //@Column(name = "nome_real", nullable = false, length = 100)
    private String nomeReal;

    //@Column(name = "nome_anonimo", length = 50)
    private String nomeAnonimo;

    //@Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    //@Column(name = "telefone_celular", length = 15, unique = true)
    private String celular;

    //@Column(nullable = false)
    private String senha;

    @CreationTimestamp
    //@Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    @ManyToOne
    //@JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;
}
