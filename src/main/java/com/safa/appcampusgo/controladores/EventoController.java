package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.dtos.EventoSimpleDTO;
import com.safa.appcampusgo.servicios.EventoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eventos")
@AllArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@RequestBody EventoDTO dto, @RequestParam Integer creadorId) {
        EventoDTO creado = eventoService.crearEvento(dto, creadorId);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<EventoDTO> listarEventos(@RequestParam(required = false) LocalDateTime fecha,
                                         @RequestParam(required = false) String categoria,
                                         @RequestParam(required = false) String institucion,
                                         @PageableDefault(size = 10) Pageable pageable) {
        return eventoService.listarEventosFiltrados(fecha, categoria, institucion, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerPorId(@PathVariable Integer id) {
        Optional<EventoDTO> dto = eventoService.obtenerEventoPorId(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> modificarEvento(@PathVariable Integer id, @RequestBody EventoDTO dto) {
        return ResponseEntity.ok(eventoService.modificarEvento(id, dto));
    }

    @GetMapping("/front")
    public List<EventoSimpleDTO> listarEventosFront() {
        return eventoService.todosEventos();
    }
}