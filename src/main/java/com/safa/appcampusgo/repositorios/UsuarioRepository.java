package com.safa.appcampusgo.repositorios;

import com.safa.appcampusgo.modelos.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
}
