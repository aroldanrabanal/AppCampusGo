package com.safa.appcampusgo.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String grupo;

    @OneToMany(mappedBy = "curso")
    @JsonIgnore
    private List<Usuarios> usuarios = new ArrayList<>();
}