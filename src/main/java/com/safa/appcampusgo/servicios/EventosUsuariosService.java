package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventosUsuariosDTO;
import com.safa.appcampusgo.mappers.EventosUsuariosMapper;
import com.safa.appcampusgo.modelos.Estado;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventosUsuariosService {
    private final EventosUsuariosRepository eventosUsuariosRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final EventosUsuariosMapper eventosUsuariosMapper;

    public EventosUsuariosDTO inscribirUsuario(Integer usuarioId, Integer eventoId, Estado estado) {
        if (eventosUsuariosRepository.findByIdUsuarioIdAndIdEventoId(usuarioId, eventoId).isPresent()) {
            throw new IllegalArgumentException("Ya inscrito");
        }
        Usuarios usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Evento evento = eventoRepository.findById(eventoId).orElseThrow();
        EventosUsuarios entity = EventosUsuarios.builder()
                .idUsuario(usuario)
                .idEvento(evento)
                .estado(estado)
                .build();
        return eventosUsuariosMapper.toDTO(eventosUsuariosRepository.save(entity));
    }

    public List<EventosUsuariosDTO> obtenerInscripcionesPorUsuario(Integer usuarioId) {
        return eventosUsuariosRepository.findByIdUsuarioId(usuarioId).stream()
                .map(eventosUsuariosMapper::toDTO)
                .collect(Collectors.toList());
    }
}