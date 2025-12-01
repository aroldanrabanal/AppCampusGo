package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.EventosUsuariosDTO;
import com.safa.appcampusgo.modelos.Estado;
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

    @PostMapping
    public ResponseEntity<EventosUsuariosDTO> inscribir(@PathVariable Integer eventoId,
                                                        @RequestParam Integer usuarioId,
                                                        @RequestParam Estado estado) {
        return new ResponseEntity<>(eventosUsuariosService.inscribirUsuario(usuarioId, eventoId, estado), HttpStatus.CREATED);
    }
}