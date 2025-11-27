package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Usuarios;
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

    // Top 5 eventos (endpoint 9).
    @GetMapping("/eventos")
    public List<Evento> top5Eventos() {
        return eventoService.obtenerTop5Eventos();
    }

    // Usuario activo (endpoint 10).
    @GetMapping("/usuarioActivo")
    public Usuarios usuarioMasActivo() {
        return usuarioService.obtenerUsuarioMasActivo();
    }
}