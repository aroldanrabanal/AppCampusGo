package com.safa.appcampusgo.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EVENTOS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;
    private LocalDateTime fecha;
    private Float precio;
    private String lugar;
    private String categoria;
    private String institucion;

    @ManyToOne
    @JoinColumn(name = "ID_CREADOR", nullable = false)
    private Usuarios creador;

    @OneToMany(mappedBy = "idEvento")
    @JsonIgnore
    private Set<EventosUsuarios> eventosUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private List<Galeria> galerias = new ArrayList<>();
}