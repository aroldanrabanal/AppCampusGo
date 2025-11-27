package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    // Para endpoint 3 - Lista con filtros (fecha, categoria, institucion).
    Page<Evento> findByFechaAfterAndCategoriaAndInstitucion(LocalDateTime fecha, String categoria, String institucion, Pageable pageable);

    // Variante sin todos los filtros (si algunos son opcionales, usa @Query).
    @Query("SELECT e FROM Evento e WHERE (:fecha is null or e.fecha > :fecha) " +
            "AND (:categoria is null or e.categoria = :categoria) " +
            "AND (:institucion is null or e.institucion = :institucion)")
    Page<Evento> findByFiltros(@Param("fecha") LocalDateTime fecha,
                               @Param("categoria") String categoria,
                               @Param("institucion") String institucion,
                               Pageable pageable);

    // Para endpoint 9 - Top 5 eventos con más asistentes.
    @Query("SELECT e, COUNT(eu) as asistentes FROM Evento e " +
            "JOIN e.eventosUsuarios eu WHERE eu.estado = 'ASISTENTE' " +
            "GROUP BY e " +
            "ORDER BY asistentes DESC")
    List<Object[]> findTop5EventosConMasAsistentes();  // Devuelve [Evento, Long], limita en servicio a 5.
}