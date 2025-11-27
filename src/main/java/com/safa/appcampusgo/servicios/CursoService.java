package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.CursosDTO;
import com.safa.appcampusgo.mappers.CursosMapper;
import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.repositorios.CursoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final CursosMapper cursosMapper;  // Inyectado

    public List<CursosDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(cursosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CursosDTO crearCurso(CursosDTO dto) {
        Cursos entity = cursosMapper.toEntity(dto);
        Cursos saved = cursoRepository.save(entity);
        return cursosMapper.toDTO(saved);
    }

    public Optional<CursosDTO> obtenerCursoPorId(Integer id) {
        return cursoRepository.findById(id).map(cursosMapper::toDTO);
    }
}