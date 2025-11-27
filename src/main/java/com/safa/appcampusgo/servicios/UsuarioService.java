package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public List<Usuarios> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Registrar usuario (endpoint 1: estudiante/profesor).
    public Usuarios registrarUsuario(Usuarios usuario) {
        // Validación: Email único.
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        return usuarioRepository.save(usuario);
    }

    // Buscar por email (para auth/login en frontend).
    public Optional<Usuarios> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Eventos participados por usuario (endpoint 8).
    public List<Evento> obtenerEventosPorUsuario(Integer usuarioId) {
        return usuarioRepository.findEventosByUsuarioId(usuarioId);
    }

    // Usuario más activo (endpoint 10: publicados + participados).
    public Usuarios obtenerUsuarioMasActivo() {
        return usuarioRepository.findUsuarioMasActivo();
    }
}