package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.servicios.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private UsuarioService usuarioService;

    @GetMapping("/all")
    public List<Usuarios> buscarTodos()
    {return usuarioService.listarUsuarios();}
}
