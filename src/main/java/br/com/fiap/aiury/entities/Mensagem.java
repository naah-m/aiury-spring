package br.com.fiap.aiury.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_mensagem")
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @ManyToOne
    //@JoinColumn(name = "id_remetente", nullable = false)
    private Usuario remetente;

    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    //@Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;
}
