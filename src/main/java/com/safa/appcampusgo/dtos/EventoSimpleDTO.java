package com.safa.appcampusgo.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoSimpleDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fecha;
    private Float precio;
    private String lugar;
    private String categoria;
}
