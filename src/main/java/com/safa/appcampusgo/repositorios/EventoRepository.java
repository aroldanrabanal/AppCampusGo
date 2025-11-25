package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
}
