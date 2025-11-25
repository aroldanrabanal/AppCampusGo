package com.safa.appcampusgo.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USUARIOS")
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

    @ManyToOne
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Cursos curso;

    @OneToMany(mappedBy = "creador")
    private List<Evento> eventosCreados;

    @OneToMany(mappedBy = "idUsuario")
    private Set<EventosUsuarios> eventosUsuarios = new HashSet<>();


}
