package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.UsuariosActivoDTO;
import com.safa.appcampusgo.dtos.UsuariosDTO;
import com.safa.appcampusgo.modelos.*;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
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
        Usuarios usuarioAngel = new Usuarios();
        usuarioAngel.setNombre("Angel");
        usuarioAngel.setApellidos("Roldán");
        usuarioAngel.setEmail("aaa@aaa.com");
        usuarioAngel.setRol(Rol.ESTUDIANTE);
        usuarioRepository.save(usuarioAngel);

        Usuarios lucia = new Usuarios();
        lucia.setNombre("Lucía");
        lucia.setApellidos("Profesora");
        lucia.setEmail("lucia@safa.es");
        lucia.setRol(Rol.PROFESOR);
        usuarioRepository.save(lucia);

        for (int i = 1; i <= 3; i++) {
            Evento evento = new Evento();
            evento.setNombre("Evento creado " + i + " por Lucía");
            evento.setFecha(LocalDateTime.now().plusDays(i));
            evento.setCreador(lucia);
            eventoRepository.save(evento);
        }

        Evento eventoAsistido = new Evento();
        eventoAsistido.setNombre("Evento al que Lucía asiste");
        eventoAsistido.setFecha(LocalDateTime.now().plusDays(10));
        eventoAsistido.setCreador(usuarioAngel);
        eventoRepository.save(eventoAsistido);

        EventosUsuarios inscrip = new EventosUsuarios();
        inscrip.setIdUsuario(lucia);
        inscrip.setIdEvento(eventoAsistido);
        inscrip.setEstado(Estado.ASISTENTE);
        eventosUsuariosRepository.save(inscrip);
    }

    @Test
     void usuarioExiste() {
        assertTrue(usuarioService.obtenerPorId(1).isPresent(),
                "El usuario debería existir");
        System.out.println("Usuario encontrado ✓");
    }

    @Test
     void usuarioNoExiste() {
        assertFalse(usuarioService.obtenerPorId(999).isPresent(),
                "El usuario no debería existir");
        System.out.println("Usuario no encontrado ✓");
    }

    @Test
     void registrarUsuario_Positive() {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setNombre("Lucía");
        dto.setApellidos("García");
        dto.setEmail("lucia@safa.es");
        dto.setRol(Rol.ESTUDIANTE);

        UsuariosDTO result = usuarioService.registrarUsuario(dto);

        assertNotNull(result.getId(), "El nuevo usuario debería tener ID asignado");
        System.out.println("Registro de usuario nuevo exitoso ✓");
    }

    @Test
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

        System.out.println("Registro fallido correctamente por email duplicado ✓");
    }

    @Test
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

        System.out.println("Eventos del usuario encontrados ✓");
    }

    @Test
     void obtenerEventosPorUsuario_Negative_UsuarioNoExiste() {
        Integer idInexistente = 999;

        List<Evento> eventos = usuarioService.obtenerEventosPorUsuario(idInexistente);

        assertNotNull(eventos, "El método nunca debería devolver null, aunque el usuario no exista");
        assertTrue(eventos.isEmpty(), "Usuario inexistente → sin eventos");

        System.out.println("Consulta de eventos para usuario inexistente devuelve lista vacía ✓");
    }

    @Test
    public void obtenerUsuarioMasActivo_Positive() {
        UsuariosActivoDTO result = usuarioService.obtenerUsuarioMasActivo2();

        assertNotNull(result);
        assertFalse(result.getEventos().isEmpty(), "Debe tener al menos algunos eventos");
        System.out.println("Usuario más activo encontrado ✓");
    }

    @Test
    public void obtenerUsuarioMasActivo_Negative_NoUsers() {
        usuarioRepository.deleteAll();

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.obtenerUsuarioMasActivo2(),
                "No hay usuarios activos");

        System.out.println("No hay usuarios activos → excepción correcta ✓");
    }
}