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
    private final CursoRepository cursoRepository;
    private final UsuariosMapper usuariosMapper;

    public List<UsuariosDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuariosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuariosDTO registrarUsuario(UsuariosDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        Usuarios entity = usuariosMapper.toEntity(dto);
        if (dto.getCursoId() != null) {
            entity.setCurso(cursoRepository.findById(dto.getCursoId()).orElseThrow());
        }
        return usuariosMapper.toDTO(usuarioRepository.save(entity));
    }

    public Optional<UsuariosDTO> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).map(usuariosMapper::toDTO);
    }

    public Optional<UsuariosDTO> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email).map(usuariosMapper::toDTO);
    }

    public List<Evento> obtenerEventosPorUsuario(Integer usuarioId) {
        return usuarioRepository.findEventosByUsuarioId(usuarioId);
    }

    public UsuariosDTO obtenerUsuarioMasActivo() {
        List<Usuarios> list = usuarioRepository.findUsuariosMasActivos();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("No hay usuarios activos");
        }
        return usuariosMapper.toDTO(list.getFirst());
    }
}