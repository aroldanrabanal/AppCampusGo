package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GaleriaRepository extends JpaRepository<Galeria, Integer> {

    // Lista toda la galería de un evento específico (para endpoint 7, GET implícito).
    // Ejemplo: findByEventoId(1) devuelve fotos de evento ID 1.
    List<Galeria> findByEventoId(Integer eventoId);

    // filtrar por fecha de subida (para la rrss por ej).
    List<Galeria> findByEventoIdAndFechaSubidaAfter(Integer eventoId, LocalDateTime fecha);
}