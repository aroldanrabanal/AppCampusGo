package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.UsuariosActivoDTO;
import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.modelos.*;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventosUsuariosRepository eventosUsuariosRepository;

    @BeforeAll
    void cargarDatos() {
        Usuarios angel = new Usuarios();
        angel.setNombre("Angel");
        angel.setApellidos("Roldán");
        angel.setEmail("aaa@aaa.com");
        angel.setRol(Rol.ESTUDIANTE);
        usuarioRepository.save(angel);

        Usuarios lucia = new Usuarios();
        lucia.setNombre("Lucía");
        lucia.setApellidos("Profesora");
        lucia.setEmail("lucia@safa.es");
        lucia.setRol(Rol.PROFESOR);
        usuarioRepository.save(lucia);

        Evento e1 = Evento.builder()
                .nombre("Taller 1")
                .fecha(LocalDateTime.now().plusDays(5))
                .creador(angel)
                .build();
        eventoRepository.save(e1);

        Evento e2 = Evento.builder()
                .nombre("Taller 2")
                .fecha(LocalDateTime.now().plusDays(10))
                .creador(angel)
                .build();
        eventoRepository.save(e2);

        Evento e3 = Evento.builder()
                .nombre("Conferencia")
                .fecha(LocalDateTime.now().plusDays(15))
                .creador(angel)
                .build();
        eventoRepository.save(e3);

        EventosUsuarios inscrip = EventosUsuarios.builder()
                .idUsuario(angel)
                .idEvento(e3)
                .estado(Estado.ASISTENTE)
                .build();
        eventosUsuariosRepository.save(inscrip);
    }
    @Test
    @DisplayName("Servicio 1 -> Usuario existe")
     void usuarioExiste() {
        assertTrue(usuarioService.obtenerPorId(1).isPresent(),
                "El usuario debería existir");
        System.out.println("Usuario encontrado ");
    }

    @Test
    @DisplayName("Servicio 1 -> Usuario no existe")
    void usuarioNoExiste() {
        assertFalse(usuarioService.obtenerPorId(999).isPresent(),
                "El usuario no debería existir");
        System.out.println("Usuario no encontrado ");
    }

    @Test
    @DisplayName("Servicio 1 -> Registrar usuario")
    void registrarUsuario_Positive() {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setNombre("Lucía");
        dto.setApellidos("García");
        dto.setEmail("lucia@safa.es");
        dto.setRol(Rol.ESTUDIANTE);

        UsuariosDTO result = usuarioService.registrarUsuario(dto);

        assertNotNull(result.getId(), "El nuevo usuario debería tener ID asignado");
        System.out.println("Registro de usuario nuevo exitoso ");
    }

    @Test
    @DisplayName("Servicio 1 -> Registrar usuario negativo")
    void registrarUsuario_Negative_EmailDuplicado() {
        UsuariosDTO dtoDuplicado = new UsuariosDTO();
        dtoDuplicado.setNombre("Otro Angel");
        dtoDuplicado.setApellidos("Roldán");
        dtoDuplicado.setEmail("aaa@aaa.com");
        dtoDuplicado.setRol(Rol.ESTUDIANTE);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(dtoDuplicado),
                "Debería lanzar excepción cuando el email ya está registrado"
        );

        assertTrue(exception.getMessage().toLowerCase().contains("email"),
                "El mensaje de error debería mencionar el problema del email");

        System.out.println("Registro fallido correctamente por email duplicado ");
    }

    @Test
    @DisplayName("Servicio 8 -> Obtener eventos por usuario")
     void obtenerEventosPorUsuario_Positive() {
        Usuarios creadorExistente = usuarioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Usuario de prueba no encontrado"));

        Evento evento = new Evento();
        evento.setNombre("Reunión de tutores");
        evento.setFecha(LocalDateTime.now().plusDays(3));
        evento.setDescripcion("Evento escolar importante");
        evento.setCreador(creadorExistente);

        eventoRepository.save(evento);

        EventosUsuarios inscripcion = new EventosUsuarios();
        inscripcion.setIdUsuario(creadorExistente);
        inscripcion.setIdEvento(evento);
        inscripcion.setEstado(Estado.ASISTENTE);
        eventosUsuariosRepository.save(inscripcion);

        List<Evento> eventos = usuarioService.obtenerEventosPorUsuario(1);

        assertFalse(eventos.isEmpty(), "El usuario debería tener al menos un evento");
        assertEquals("Reunión de tutores", eventos.getFirst().getNombre());

        System.out.println("Eventos del usuario encontrados ");
    }

    @Test
    @DisplayName("Servicio 8 -> Obtener eventos por usuario negativo")
     void obtenerEventosPorUsuario_Negative_UsuarioNoExiste() {
        Integer idInexistente = 999;

        List<Evento> eventos = usuarioService.obtenerEventosPorUsuario(idInexistente);

        assertTrue(eventos.isEmpty(), "Usuario inexistente → sin eventos");

        System.out.println("Consulta de eventos para usuario inexistente devuelve lista vacía ");
    }

    @Test
    @DisplayName("Servicio 10 -> Usuario mas activo")
    public void obtenerUsuarioMasActivo_Positive() {
        UsuariosActivoDTO result = usuarioService.obtenerUsuarioMasActivo2();

        assertNotNull(result);
        assertEquals("Angel", result.getNombreUsuario());
        assertFalse(result.getEventos().isEmpty(), "Debe tener eventos (creados o asistidos)");
        System.out.println("Usuario más activo encontrado ");
    }

    @Test
    @DisplayName("Servicio 10 -> Usuario mas activo negativo")
    void obtenerUsuarioMasActivo_Negative_SinActividad() {
        eventosUsuariosRepository.deleteAll();
        eventoRepository.deleteAll();

        UsuariosActivoDTO result = usuarioService.obtenerUsuarioMasActivo2();

        assertTrue(result.getEventos().isEmpty(),
                "Si no hay actividad, la lista de eventos debe estar vacía");

        System.out.println("Usuario sin actividad → eventos vacíos ");
    }
}