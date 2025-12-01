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
    private final EventoMapper eventoMapper;

    public EventoDTO crearEvento(EventoDTO dto, Integer creadorId) {
        Usuarios creador = usuarioRepository.findById(creadorId).orElseThrow();
        Evento entity = eventoMapper.toEntity(dto);
        entity.setCreador(creador);
        Evento saved = eventoRepository.save(entity);
        return eventoMapper.toDTO(saved);
    }

    public Page<EventoDTO> listarEventosFiltrados(LocalDateTime fecha, String categoria, String institucion, Pageable pageable) {
        return eventoRepository.findByFiltros(fecha, categoria, institucion, pageable).map(eventoMapper::toDTO);
    }

    public Optional<EventoDTO> obtenerEventoPorId(Integer id) {
        return eventoRepository.findById(id).map(eventoMapper::toDTO);
    }

    public EventoDTO modificarEvento(Integer id, EventoDTO dto) {
        Evento entity = eventoRepository.findById(id).orElseThrow();
        eventoMapper.updateEvento(dto, entity);
        return eventoMapper.toDTO(eventoRepository.save(entity));
    }

    public List<EventoDTO> obtenerTop5Eventos() {
        List<Object[]> results = eventoRepository.findTop5EventosConMasAsistentes();
        return results.stream().limit(5)
                .map(result -> eventoMapper.toDTO((Evento) result[0]))
                .collect(Collectors.toList());
    }
}