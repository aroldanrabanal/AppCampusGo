package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Cursos, Integer> {
}
