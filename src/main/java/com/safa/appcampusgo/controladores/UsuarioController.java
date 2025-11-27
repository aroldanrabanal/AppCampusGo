package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Usuarios;
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
    public List<Usuarios> buscarTodos() {
        return usuarioService.listarUsuarios();
    }

    // Registro de usuario (endpoint 1: estudiante/profesor).
    @PostMapping
    public ResponseEntity<Usuarios> registrarUsuario(@RequestBody Usuarios usuario) {
        Usuarios registrado = usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    // Detalles por ID.
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> obtenerPorId(@PathVariable Integer id) {
        Optional<Usuarios> usuario = usuarioService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eventos en los que participa (endpoint 8).
    @GetMapping("/{id}/eventos")
    public List<Evento> obtenerEventosPorUsuario(@PathVariable Integer id) {
        return usuarioService.obtenerEventosPorUsuario(id);
    }
}