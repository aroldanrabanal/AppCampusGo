package com.safa.appcampusgo.dtos;

import com.safa.appcampusgo.modelos.Estado;
import lombok.Data;

@Data
public class EventosUsuariosDTO {
    private Integer id;
    private Estado estado;
    private Integer usuarioId;
    private String usuarioNombre;
    private Integer eventoId;
    private String eventoNombre;
}