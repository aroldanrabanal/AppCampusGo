package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Cursos;
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
    public List<Cursos> buscarTodos() {
        return cursoService.listarCursos();
    }

    // Usa servicio para validaciones.
    @PostMapping
    public ResponseEntity<Cursos> crearCurso(@RequestBody Cursos curso) {
        Cursos creado = cursoService.crearCurso(curso);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    // Detalles por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Cursos> obtenerPorId(@PathVariable Integer id) {
        Optional<Cursos> curso = cursoService.obtenerCursoPorId(id);
        return curso.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}