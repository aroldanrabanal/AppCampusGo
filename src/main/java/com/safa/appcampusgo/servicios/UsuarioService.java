package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public List<Usuarios> listarUsuarios(){
        return usuarioRepository.findAll();
    }
}
