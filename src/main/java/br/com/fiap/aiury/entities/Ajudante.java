package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_AJUDANTE")
public class Ajudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 🔑 Chave primária

    @Column(name = "area_atuacao", length = 100, nullable = false)
    private String areaAtuacao;

    @Lob
    @Column(name = "motivacao")
    private String motivacao;

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    @Column(name = "rating")
    private Double rating;
}
