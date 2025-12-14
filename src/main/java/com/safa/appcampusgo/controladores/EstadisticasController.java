package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.dtos.TopEventoDTO;
import com.safa.appcampusgo.dtos.UsuariosActivoDTO;
import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.servicios.EventoService;
import com.safa.appcampusgo.servicios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
@AllArgsConstructor
public class EstadisticasController {

    private final EventoService eventoService;
    private final UsuarioService usuarioService;

    @GetMapping("/eventos")
    public List<TopEventoDTO> top5Eventos() {
        return eventoService.obtenerTop5Eventos();
    }
    @GetMapping("/usuarioActivo")
    public UsuariosActivoDTO usuarioMasActivo() {
        return usuarioService.obtenerUsuarioMasActivo();
    }
}