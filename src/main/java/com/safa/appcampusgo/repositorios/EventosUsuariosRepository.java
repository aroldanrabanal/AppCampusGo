package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.modelos.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventosUsuariosRepository extends JpaRepository<EventosUsuarios, Integer> {

    // Check si usuario ya está inscrito en evento (para evitar duplicados, endpoint 6).
    Optional<EventosUsuarios> findByIdUsuarioIdAndIdEventoId(Integer usuarioId, Integer eventoId);

    // Lista inscripciones por usuario y estado (útil para recordatorios).
    List<EventosUsuarios> findByIdUsuarioIdAndEstado(Integer usuarioId, Estado estado);
    
    // Lista inscripciones por usuario
    List<EventosUsuarios> findByIdUsuarioId(Integer usuarioId);
}