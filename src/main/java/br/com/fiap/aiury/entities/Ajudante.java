package br.com.fiap.aiury.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
//@Table(name = "ajudante")
//@PrimaryKeyJoinColumn(name = "id_usuario")
public class Ajudante {

    //@Column(name = "area_atuacao", length = 100) // Conforme modelo conceitual
    private String areaAtuacao;

    //@Column(name = "motivacao", columnDefinition = "TEXT") // Novo campo 'motivacao'
    private String motivacao;

    //@Column(name = "disponibilidade", columnDefinition = "boolean default false")
    private boolean isDisponivel;

    //@Column(name = "rating")
    private Double rating;
}
