package com.safa.appcampusgo.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fecha;
    private Float precio;
    private String lugar;
    private String categoria;
    private String institucion;
    private Integer creadorId;
    private String creadorNombre;
}