package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.modelos.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    Page<Evento> findByFechaAfterAndCategoriaAndInstitucion(LocalDateTime fecha, String categoria, String institucion, Pageable pageable);

    @Query("SELECT u.nombre FROM Usuarios u JOIN u.eventosUsuarios eu WHERE eu.idEvento.id = :eventoId AND eu.estado = 'ASISTENTE'")
    List<String> findAsistentesNombresByEventoId(@Param("eventoId") Integer eventoId);

    @Query("SELECT e FROM Evento e WHERE (:fecha is null or e.fecha > :fecha) " +
            "AND (:categoria is null or e.categoria = :categoria) " +
            "AND (:institucion is null or e.institucion = :institucion)")
    Page<Evento> findByFiltros(@Param("fecha") LocalDateTime fecha,
                               @Param("categoria") String categoria,
                               @Param("institucion") String institucion,
                               Pageable pageable);

    @Query(value = "SELECT e.nombre AS nombreEvento, " +
            "COUNT(eu.id) AS numAsistentes, " +
            "STRING_AGG(u.nombre, ', ') AS nombresAsistentes " +
            "FROM campusgo.eventos e " +
            "LEFT JOIN campusgo.eventos_usuarios eu ON e.id = eu.id_eventos " +
            "LEFT JOIN campusgo.usuarios u ON eu.id_usuarios = u.id " +
            "WHERE eu.estado = 'ASISTENTE' " +
            "GROUP BY e.id " +
            "ORDER BY numAsistentes DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5EventosConAsistentes();

    List<Evento> findByCreador(Usuarios creador);



}