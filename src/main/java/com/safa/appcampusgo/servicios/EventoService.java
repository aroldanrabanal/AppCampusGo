package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.dtos.EventoSimpleDTO;
import com.safa.appcampusgo.dtos.TopEventoDTO;
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
import java.util.*;
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
        Page<Evento> page = eventoRepository.findByFiltros(fecha, categoria, institucion, pageable);
        return page.map(entity -> {
            EventoDTO dto = eventoMapper.toDTO(entity);
            List<String> asistentes = eventoRepository.findAsistentesNombresByEventoId(entity.getId());
            dto.setAsistentes(asistentes);
            return dto;
        });
    }

    public EventoDTO modificarEvento(Integer id, EventoDTO dto) {
        Evento entity = eventoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Evento no encontrado"));
        eventoMapper.updateEvento(dto, entity);
        return eventoMapper.toDTO(eventoRepository.save(entity));
    }

    public Optional<EventoDTO> obtenerEventoPorId(Integer id) {
        Optional<Evento> entityOpt = eventoRepository.findById(id);
        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }
        Evento entity = entityOpt.get();
        EventoDTO dto = eventoMapper.toDTO(entity);
        List<String> asistentes = eventoRepository.findAsistentesNombresByEventoId(id);
        dto.setAsistentes(asistentes);
        return Optional.of(dto);
    }

    public List<EventoSimpleDTO> todosEventos() {
        List<Evento> eventos = eventoRepository.findAll();
        return eventos.stream().map(evento -> {
            EventoSimpleDTO dto = new EventoSimpleDTO();
            dto.setNombre(evento.getNombre());
            dto.setDescripcion(evento.getDescripcion());
            dto.setFecha(evento.getFecha());
            dto.setPrecio(evento.getPrecio());
            dto.setLugar(evento.getLugar());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<TopEventoDTO> obtenerTop5Eventos() {
        List<Object[]> results = eventoRepository.findTop5EventosConAsistentes();
        List<TopEventoDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            TopEventoDTO dto = new TopEventoDTO();
            dto.setNombreEvento((String) row[0]);
            dto.setNumAsistentes((Long) row[1]);
            String nombresStr = (String) row[2];
            dto.setNombresAsistentes(nombresStr != null ? Arrays.asList(nombresStr.split(", ")) : new ArrayList<>());  // Split a lista
            dtos.add(dto);
        }
        return dtos;
    }
}