package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//@Table(name = "cidade")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_cidade")
    private Long id;

    //@Column(name = "nome_cidade", nullable = false, length = 100)
    private String nomeCidade;

    @ManyToOne
    //@JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

}
