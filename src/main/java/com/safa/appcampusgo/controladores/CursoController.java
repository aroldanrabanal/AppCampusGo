package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.CursosDTO;
import com.safa.appcampusgo.servicios.CursoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
@AllArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping("/all")
    public List<CursosDTO> buscarTodos() {
        return cursoService.listarCursos();
    }

    @PostMapping
    public ResponseEntity<CursosDTO> crearCurso(@RequestBody CursosDTO dto) {
        return new ResponseEntity<>(cursoService.crearCurso(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursosDTO> obtenerPorId(@PathVariable Integer id) {
        Optional<CursosDTO> dto = cursoService.obtenerCursoPorId(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}