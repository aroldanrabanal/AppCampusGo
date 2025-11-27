package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Evento;
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

@Service
@AllArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    // Crear evento (endpoint 2).
    public Evento crearEvento(Evento evento, Integer creadorId) {
        Optional<Usuarios> creador = usuarioRepository.findById(creadorId);
        if (creador.isEmpty()) {
            throw new IllegalArgumentException("No esta definido el usuario");
        }
        evento.setCreador(creador.get());
        return eventoRepository.save(evento);
    }

    // Lista con filtros (endpoint 3: fecha, categoria, institucion).
    public Page<Evento> listarEventosFiltrados(LocalDateTime fecha, String categoria, String institucion, Pageable pageable) {
        return eventoRepository.findByFiltros(fecha, categoria, institucion, pageable);
    }

    // Detalles de evento (endpoint 4).
    public Optional<Evento> obtenerEventoPorId(Integer id) {
        return eventoRepository.findById(id);
    }

    // Modificar evento (endpoint 5: horario, lugar, desc).
    public Evento modificarEvento(Integer id, Evento updates) {
        Evento existente = eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        // Actualiza campos (evita sobreescribir creador).
        if (updates.getNombre() != null) existente.setNombre(updates.getNombre());
        if (updates.getDescripcion() != null) existente.setDescripcion(updates.getDescripcion());
        // ... para mas campos es mas de lo mismo
        return eventoRepository.save(existente);
    }

    // Top 5 eventos con más asistentes (endpoint 9).
    public List<Evento> obtenerTop5Eventos() {
        List<Object[]> results = eventoRepository.findTop5EventosConMasAsistentes();
        return results.stream().limit(5).map(result -> (Evento) result[0]).toList();
    }
}