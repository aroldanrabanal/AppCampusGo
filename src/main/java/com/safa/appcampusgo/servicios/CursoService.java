package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.CursosDTO;
import com.safa.appcampusgo.mappers.CursosMapper;
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
    private final CursosMapper cursosMapper;

    public List<CursosDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(cursosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CursosDTO crearCurso(CursosDTO dto) {
        return cursosMapper.toDTO(cursoRepository.save(cursosMapper.toEntity(dto)));
    }

    public Optional<CursosDTO> obtenerCursoPorId(Integer id) {
        return cursoRepository.findById(id).map(cursosMapper::toDTO);
    }
}