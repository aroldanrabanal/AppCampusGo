package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.servicios.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/eventos")
@AllArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    // Crear evento (endpoint 2).
    @PostMapping
    public ResponseEntity<Evento> crearEvento(@RequestBody Evento evento, @RequestParam Integer creadorId) {
        Evento creado = eventoService.crearEvento(evento, creadorId);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    // Lista con filtros (endpoint 3: usa @RequestParam para opcionales, Pageable para paginación en Ionic).
    @GetMapping
    public Page<Evento> listarEventos(@RequestParam(required = false) LocalDateTime fecha,
                                      @RequestParam(required = false) String categoria,
                                      @RequestParam(required = false) String institucion,
                                      @PageableDefault(size = 10) Pageable pageable) {
        return eventoService.listarEventosFiltrados(fecha, categoria, institucion, pageable);
    }

    // Detalles (endpoint 4).
    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerPorId(@PathVariable Integer id) {
        Optional<Evento> evento = eventoService.obtenerEventoPorId(id);
        return evento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Modificar (endpoint 5).
    @PutMapping("/{id}")
    public ResponseEntity<Evento> modificarEvento(@PathVariable Integer id, @RequestBody Evento updates) {
        Evento modificado = eventoService.modificarEvento(id, updates);
        return ResponseEntity.ok(modificado);
    }
}