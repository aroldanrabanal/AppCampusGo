package com.safa.appcampusgo.dtos;

import lombok.Data;
import java.util.List;

@Data
public class UsuariosActivoDTO {
    private String nombreUsuario;
    private List<EventoTipoDTO> eventos;

    @Data
    public static class EventoTipoDTO {
        private String nombreEvento;
        private String tipo;
    }
}