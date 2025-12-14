package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GaleriaRepository extends JpaRepository<Galeria, Integer> {

    List<Galeria> findByEventoId(Integer eventoId);

    List<Galeria> findByEventoIdAndFechaSubidaAfter(Integer eventoId, LocalDateTime fecha);
}