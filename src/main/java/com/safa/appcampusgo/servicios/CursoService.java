package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.repositorios.CursoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;

    public List<Cursos> listarCursos() {
        return cursoRepository.findAll();
    }
}
