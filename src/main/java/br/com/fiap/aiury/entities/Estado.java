package br.com.fiap.aiury.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
//@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_estado")
    private Long id;

    //@Column(name = "nome_estado", nullable = false, length = 50, unique = true)
    private String nomeEstado;

    //@Column(name = "uf", nullable = false, length = 2, unique = true)
    private String uf;
}
