package com.safa.appcampusgo.dtos;

import lombok.Data;
import java.util.List;

@Data
public class TopEventoDTO {
    private String nombreEvento;
    private Long numAsistentes;
    private List<String> nombresAsistentes;
}