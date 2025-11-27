package com.safa.appcampusgo.dtos;

import com.safa.appcampusgo.modelos.Rol;
import lombok.Data;

@Data
public class UsuariosDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private Rol rol;
    private Integer cursoId;
    private String cursoNombre;
    private String cursoGrupo;
}