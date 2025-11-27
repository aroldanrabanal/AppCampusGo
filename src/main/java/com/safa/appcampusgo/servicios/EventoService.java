package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.mappers.EventoMapper;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoMapper eventoMapper;  // Inyectado

    public EventoDTO crearEvento(EventoDTO dto, Integer creadorId) {
        Optional<Usuarios> creador = usuarioRepository.findById(creadorId);
        if (creador.isEmpty() || !creador.get().getRol().equals(Rol.PROFESOR)) {
            throw new IllegalArgumentException("Solo profesores pueden crear eventos");
        }
        Evento entity = eventoMapper.toEntity(dto);
        entity.setCreador(creador.get());
        Evento saved = eventoRepository.save(entity);
        return eventoMapper.toDTO(saved);
    }

    public Page<EventoDTO> listarEventosFiltrados(LocalDateTime fecha, String categoria, String institucion, Pageable pageable) {
        return eventoRepository.findByFiltros(fecha, categoria, institucion, pageable)
                .map(eventoMapper::toDTO);
    }

    public Optional<EventoDTO> obtenerEventoPorId(Integer id) {
        return eventoRepository.findById(id).map(eventoMapper::toDTO);
    }

    public EventoDTO modificarEvento(Integer id, EventoDTO dto) {
        Evento existente = eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        // Actualiza campos con mapper (usa @MappingTarget si expandes para merges avanzados, PDF p.16)
        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        // ... similar para otros
        Evento saved = eventoRepository.save(existente);
        return eventoMapper.toDTO(saved);
    }

    public List<EventoDTO> obtenerTop5Eventos() {
        List<Object[]> results = eventoRepository.findTop5EventosConMasAsistentes();
        return results.stream().limit(5)
                .map(result -> eventoMapper.toDTO((Evento) result[0]))
                .collect(Collectors.toList());
    }
}