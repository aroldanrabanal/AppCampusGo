package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void cargarDatos(){
        Usuarios usuarioTest;
        usuarioTest = new Usuarios();
        usuarioTest.setNombre("Angel");
        usuarioTest.setApellidos("Roldán");
        usuarioTest.setEmail("aaa@aaa.com");
        usuarioTest.setRol(Rol.ESTUDIANTE);
        usuarioRepository.save(usuarioTest);
    }

    @Test
    public void usuarioExiste() {
        assertTrue(usuarioService.obtenerPorId(1).isPresent(),
                "El usuario debería existir");
        System.out.println("Usuario encontrado ✓");
    }

    @Test
    public void usuarioNoExiste() {
        assertFalse(usuarioService.obtenerPorId(999).isPresent(),
                "El usuario no debería existir");
        System.out.println("Usuario no encontrado ✓");
    }
}
