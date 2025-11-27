package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.repositorios.CursoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;

    public List<Cursos> listarCursos() {
        return cursoRepository.findAll();
    }

    // Crear curso (para admins/profesores en futuro).
    public Cursos crearCurso(Cursos curso) {
        // Validación simple: No duplicados por nombre/grupo.
        Optional<Cursos> existente = cursoRepository.findByNombreAndGrupo(curso.getNombre(), curso.getGrupo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Curso ya existe");
        }
        return cursoRepository.save(curso);
    }

    // Buscar por ID (para detalles).
    public Optional<Cursos> obtenerCursoPorId(Integer id) {
        return cursoRepository.findById(id);
    }
}