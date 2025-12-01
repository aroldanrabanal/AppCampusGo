package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {

    // Método derivado para buscar por email (útil para login/registro, endpoint 1).
    Optional<Usuarios> findByEmail(String email);

    // Método derivado para filtrar por rol (ej. lista de profesores).
    List<Usuarios> findByRol(Rol rol);

    //  Para endpoint 8 - Eventos en los que participa un usuario (relacional).
    @Query("SELECT eu.idEvento FROM EventosUsuarios eu WHERE eu.idUsuario.id = :usuarioId")
    List<Evento> findEventosByUsuarioId(Integer usuarioId);

    //  Para endpoint 10 - Usuario más activo (publicados + participados).
    // Cuenta eventos creados + inscripciones, ordena descendente, limita a 1.
    @Query("SELECT u FROM Usuarios u " +
            "LEFT JOIN u.eventosCreados ec " +
            "LEFT JOIN u.eventosUsuarios eu " +
            "GROUP BY u " +
            "ORDER BY (COUNT(ec) + COUNT(eu)) DESC")
    List<Usuarios> findUsuariosMasActivos();;
}