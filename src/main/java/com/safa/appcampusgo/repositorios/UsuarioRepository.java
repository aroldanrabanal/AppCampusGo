package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByEmail(String email);

    List<Usuarios> findByRol(Rol rol);

    @Query("SELECT eu.idEvento FROM EventosUsuarios eu WHERE eu.idUsuario.id = :usuarioId")
    List<Evento> findEventosByUsuarioId(Integer usuarioId);

    @Query(value = "WITH top_user AS (SELECT u.id, u.nombre FROM usuarios u ORDER BY ( (SELECT COUNT(*) FROM eventos e WHERE e.id_creador = u.id) + (SELECT COUNT(*) FROM eventos_usuarios eu WHERE eu.id_usuarios = u.id AND eu.estado = 'ASISTENTE') ) DESC LIMIT 1) " +
            "SELECT top.nombre AS userNombre, e.nombre AS eventoNombre, 'CREADO' AS tipo FROM top_user top LEFT JOIN eventos e ON top.id = e.id_creador " +
            "UNION ALL " +
            "SELECT top.nombre AS userNombre, ev.nombre AS eventoNombre, 'ASISTIDO' AS tipo FROM top_user top LEFT JOIN eventos_usuarios eu ON top.id = eu.id_usuarios LEFT JOIN eventos ev ON eu.id_eventos = ev.id WHERE eu.estado = 'ASISTENTE'",
            nativeQuery = true)
    List<Object[]> findTopUsuarioConEventos();
}