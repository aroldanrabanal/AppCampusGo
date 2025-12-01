package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.servicios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/all")
    public List<UsuariosDTO> buscarTodos() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    public ResponseEntity<UsuariosDTO> registrarUsuario(@RequestBody UsuariosDTO dto) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> obtenerPorId(@PathVariable Integer id) {
        Optional<UsuariosDTO> dto = usuarioService.obtenerPorId(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/eventos")
    public List<Evento> obtenerEventosPorUsuario(@PathVariable Integer id) {
        return usuarioService.obtenerEventosPorUsuario(id);
    }
}