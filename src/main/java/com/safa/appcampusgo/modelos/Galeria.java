package com.safa.appcampusgo.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "galeria",  schema = "campusgo")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Galeria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_EVENTO", nullable = false)
    private Evento evento;

    private String url;
    private String descripcion;
    private LocalDateTime fechaSubida;
}