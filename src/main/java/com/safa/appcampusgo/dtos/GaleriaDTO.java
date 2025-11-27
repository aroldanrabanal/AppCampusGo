package com.safa.appcampusgo.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GaleriaDTO {
    private Integer id;
    private Integer eventoId;
    private String url;
    private String descripcion;
    private LocalDateTime fechaSubida;
}