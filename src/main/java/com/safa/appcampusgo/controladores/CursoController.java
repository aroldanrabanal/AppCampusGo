package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.repositorios.CursoRepository;
import com.safa.appcampusgo.servicios.CursoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@AllArgsConstructor
public class CursoController {

    private CursoService cursoService;
    private final CursoRepository cursoRepository;

    @GetMapping("/all")
    public List<Cursos> buscarTodos() {
        return cursoService.listarCursos();
    }

    @PostMapping
    public Cursos crearCurso(@RequestBody Cursos curso) {
        return cursoRepository.save(curso);
    }
}
