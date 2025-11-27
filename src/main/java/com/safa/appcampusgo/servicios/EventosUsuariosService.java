package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Estado;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventosUsuariosService {
    private final EventosUsuariosRepository eventosUsuariosRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;

    // Inscribir usuario (endpoint 6).
    public EventosUsuarios inscribirUsuario(Integer usuarioId, Integer eventoId, Estado estado) {
        // Validar existencia.
        Usuarios usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        // Evitar duplicados.
        if (eventosUsuariosRepository.findByIdUsuarioIdAndIdEventoId(usuarioId, eventoId).isPresent()) {
            throw new IllegalArgumentException("Ya inscrito");
        }
        EventosUsuarios inscripcion = EventosUsuarios.builder()
                .idUsuario(usuario)
                .idEvento(evento)
                .estado(estado)
                .build();
        return eventosUsuariosRepository.save(inscripcion);
    }

    // Lista inscripciones por usuario (para recordatorios en frontend).
    public List<EventosUsuarios> obtenerInscripcionesPorUsuario(Integer usuarioId) {
        return eventosUsuariosRepository.findByIdUsuarioId(usuarioId);
    }
}