package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Estado;
import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.servicios.EventosUsuariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventos/{eventoId}/inscripciones")
@AllArgsConstructor
public class EventosUsuariosController {

    private final EventosUsuariosService eventosUsuariosService;

    // Inscribir (endpoint 6: usa @RequestParam para estado y usuarioId).
    @PostMapping
    public ResponseEntity<EventosUsuarios> inscribir(@PathVariable Integer eventoId,
                                                     @RequestParam Integer usuarioId,
                                                     @RequestParam Estado estado) {
        EventosUsuarios inscripcion = eventosUsuariosService.inscribirUsuario(usuarioId, eventoId, estado);
        return new ResponseEntity<>(inscripcion, HttpStatus.CREATED);
    }
}