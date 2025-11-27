package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.mappers.UsuariosMapper;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.CursoRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;  // Para asignar curso completo
    private final UsuariosMapper usuariosMapper;    // Inyectado

    public List<UsuariosDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuariosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuariosDTO registrarUsuario(UsuariosDTO dto) {
        Usuarios entity = usuariosMapper.toEntity(dto);
        // Asigna curso completo para evitar nulls
        if (dto.getCursoId() != null) {
            entity.setCurso(cursoRepository.findById(dto.getCursoId()).orElseThrow(() -> new IllegalArgumentException("Curso no encontrado")));
        }
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        Usuarios saved = usuarioRepository.save(entity);
        return usuariosMapper.toDTO(saved);
    }

    public Optional<UsuariosDTO> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).map(usuariosMapper::toDTO);
    }

    public Optional<UsuariosDTO> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuariosMapper::toDTO);
    }

    // Para endpoint 8: Eventos por usuario
    public List<Evento> obtenerEventosPorUsuario(Integer usuarioId) {
        return usuarioRepository.findEventosByUsuarioId(usuarioId);
    }

    // Para endpoint 10: Usuario más activo (devuelve DTO)
    public UsuariosDTO obtenerUsuarioMasActivo() {
        Usuarios entity = usuarioRepository.findUsuarioMasActivo();
        return usuariosMapper.toDTO(entity);
    }
}