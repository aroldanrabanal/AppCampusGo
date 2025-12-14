package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Cursos, Integer> {

    Optional<Cursos> findByNombreAndGrupo(String nombre, String grupo);
}