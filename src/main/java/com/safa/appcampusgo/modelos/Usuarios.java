package com.safa.appcampusgo.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuarios", schema = "campusgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellidos;
    private String email;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "ID_CURSO", nullable = true)
    private Cursos curso;

    @OneToMany(mappedBy = "creador")
    @JsonIgnore
    private List<Evento> eventosCreados;

    @OneToMany(mappedBy = "idUsuario")
    @JsonIgnore
    private Set<EventosUsuarios> eventosUsuarios = new HashSet<>();
}