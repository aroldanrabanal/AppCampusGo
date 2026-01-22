package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.mappers.UsuariosMapper;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioServiceIntegrationTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuariosMapper usuariosMapper;

    @Test
    @DisplayName("Servicio 1 -> Integración: Registrar usuario cuando NO existe")
    void registrarUsuarioNoExiste() {
        // Preparar datos de entrada (DTO)
        UsuariosDTO dto = new UsuariosDTO();
        dto.setNombre("Angel");
        dto.setApellidos("Roldán");
        dto.setEmail("aaa@aaa.com");
        dto.setRol(Rol.ESTUDIANTE);

        // Preparar entity esperado
        Usuarios angel = new Usuarios();
        angel.setId(1);
        angel.setNombre("Angel");
        angel.setApellidos("Roldán");
        angel.setEmail("aaa@aaa.com");
        angel.setRol(Rol.ESTUDIANTE);

        // Mockear dependencias
        when(usuarioRepository.findByEmail("aaa@aaa.com")).thenReturn(Optional.empty()); // Usuario NO existe
        when(usuariosMapper.toEntity(dto)).thenReturn(angel); // Mapper crea entity correctamente
        when(usuarioRepository.save(any(Usuarios.class))).thenReturn(angel); // Save retorna entity salvado
        when(usuariosMapper.toDTO(angel)).thenReturn(dto); // Mapper de vuelta a DTO

        // Ejecutar el método
        UsuariosDTO resultado = usuarioService.registrarUsuario(dto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("aaa@aaa.com", resultado.getEmail()); // Verifica salida

        verify(usuarioRepository).findByEmail("aaa@aaa.com"); // Se llamó a la verificación de email
        verify(usuariosMapper).toEntity(dto); // Se llamó al mapper toEntity
        verify(usuarioRepository).save(angel); // Se llamó a save con el entity correcto (usa eq(angel) si quieres exacto, o any())
        verify(usuariosMapper).toDTO(angel); // Se llamó al mapper toDTO
    }
}
