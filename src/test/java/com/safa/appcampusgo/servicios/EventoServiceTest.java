package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.*;
import com.safa.appcampusgo.modelos.*;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventoServiceTest {

    @Autowired private EventoService eventoService;
    @Autowired private EventosUsuariosService eventosUsuariosService;
    @Autowired private GaleriaService galeriaService;

    @Autowired private EventoRepository eventoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @Autowired private EventosUsuariosRepository eventosUsuariosRepository;

    private Usuarios profesor;
    private Evento eventoComun;

    @BeforeAll
    void cargarDatos() {
        profesor = Usuarios.builder()
                .nombre("Profesor Test")
                .apellidos("SAFA")
                .email("profesor@safa.es")
                .rol(Rol.PROFESOR)
                .build();
        usuarioRepository.save(profesor);

        eventoComun = Evento.builder()
                .nombre("Fiesta fin de curso")
                .descripcion("Celebración con todos los alumnos")
                .fecha(LocalDateTime.now().plusDays(10))
                .lugar("Patio principal")
                .categoria("Fiesta")
                .institucion("SAFA")
                .creador(profesor)
                .build();
        eventoRepository.save(eventoComun);
    }

    @Test
    @DisplayName("Servicio 2 -> Registrar evento")
    void crearEvento_Positive() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Taller de robótica 2026");
        dto.setDescripcion("Taller práctico para alumnos de secundaria");
        dto.setFecha(LocalDateTime.now().plusDays(20));
        dto.setLugar("Aula de informática");
        dto.setCategoria("Tecnología");
        dto.setInstitucion("SAFA");
        dto.setPrecio(0f);

        EventoDTO result = eventoService.crearEvento(dto, profesor.getId());

        assertNotNull(result.getId(), "El nuevo evento debe tener ID asignado");
        assertEquals("Taller de robótica 2026", result.getNombre());
        assertEquals(profesor.getId(), result.getCreadorId());
        System.out.println("Nuevo evento creado correctamente ");
    }

    @Test
    @DisplayName("Servicio 2 -> Registrar evento negativo")
    void crearEvento_Negative_CreadorNoExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Evento inválido");
        dto.setFecha(LocalDateTime.now().plusDays(5));

        assertThrows(NoSuchElementException.class,
                () -> eventoService.crearEvento(dto, 999),
                "Debe lanzar excepción si el creador no existe en BD");
        System.out.println("Creación rechazada por creador inexistente ");
    }

    @Test
    @DisplayName("Servicio 3 -> Listar eventos")
    void listarEventosFiltrados_Positive() {
        Page<EventoDTO> result = eventoService.listarEventosFiltrados(
                null, "Fiesta", "SAFA", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("Fiesta fin de curso", result.getContent().getFirst().getNombre());
        System.out.println("Listado de eventos con filtros correcto ");
    }

    @Test
    @DisplayName("Servicio 3 -> Listar eventos negativo")
    void listarEventosFiltrados_Negative_SinResultados() {
        Page<EventoDTO> result = eventoService.listarEventosFiltrados(
                LocalDateTime.now().plusYears(1), "Inexistente", "SAFA", PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
        System.out.println("No resultados con filtros inválidos ");
    }

    @Test
    @DisplayName("Servicio 4 -> Obtener evento por ID")
    void obtenerEventoPorId_Positive() {
        Optional<EventoDTO> result = eventoService.obtenerEventoPorId(eventoComun.getId());

        assertTrue(result.isPresent());
        assertEquals("Fiesta fin de curso", result.get().getNombre());
        System.out.println("Detalle de evento obtenido ");
    }

    @Test
    @DisplayName("Servicio 4 -> Obtener evento por ID negativo")
    void obtenerEventoPorId_Negative_NoExiste() {
        Optional<EventoDTO> result = eventoService.obtenerEventoPorId(999);

        assertFalse(result.isPresent());
        System.out.println("Evento inexistente devuelve vacío ");
    }

    @Test
    @DisplayName("Servicio 5 -> Modificar evento")
    void modificarEvento_Positive() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Fiesta fin de curso - ACTUALIZADA");
        dto.setDescripcion("Nueva descripción");

        EventoDTO result = eventoService.modificarEvento(eventoComun.getId(), dto);

        assertEquals("Fiesta fin de curso - ACTUALIZADA", result.getNombre());
        System.out.println("Evento modificado correctamente ");
    }

    @Test
    @DisplayName("Servicio 5 -> Modificar evento negativo")
    void modificarEvento_Negative_NoExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Intento fallido");

        assertThrows(Exception.class,
                () -> eventoService.modificarEvento(999, dto),
                "Debe fallar si no existe");
        System.out.println("Modificación rechazada por ID inexistente ");
    }

    @Test
    @DisplayName("Servicio 6 -> Inscribir usuarios")
    void inscribirUsuario_Positive() {
        EventosUsuariosDTO result = eventosUsuariosService.inscribirUsuario(
                profesor.getId(), eventoComun.getId(), Estado.ASISTENTE);

        assertNotNull(result.getId());
        assertEquals(Estado.ASISTENTE, result.getEstado());
        System.out.println("Inscripción como ASISTENTE realizada ");
    }

    @Test
    @DisplayName("Servicio 6 -> Inscribir usuarios negativo")
    void inscribirUsuario_Negative_YaInscrito() {
        eventosUsuariosService.inscribirUsuario(profesor.getId(), eventoComun.getId(), Estado.INTERESADO);

        assertThrows(IllegalArgumentException.class,
                () -> eventosUsuariosService.inscribirUsuario(profesor.getId(), eventoComun.getId(), Estado.ASISTENTE),
                "Ya inscrito");
        System.out.println("Inscripción duplicada rechazada ");
    }

    @Test
    @DisplayName("Servicio 7 -> Subir fotos")
    void subirMultimedia_Positive() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto-fiesta.jpg", "image/jpeg", "foto de prueba".getBytes());

        GaleriaDTO result = galeriaService.subirMultimedia(eventoComun.getId(), file, "Foto de la fiesta");

        assertNotNull(result.getId());
        assertTrue(result.getUrl().contains("/uploads/"));
        assertEquals("Foto de la fiesta", result.getDescripcion());
        System.out.println("Foto subida correctamente al evento ");
    }

    @Test
    @DisplayName("Servicio 7 -> Subir fotos negativo")
    void subirMultimedia_Negative_EventoNoExiste() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto-invalida.jpg", "image/jpeg", "contenido".getBytes());

        assertThrows(Exception.class,
                () -> galeriaService.subirMultimedia(999, file, "Foto inválida"),
                "Debe fallar si el evento no existe");
        System.out.println("Subida rechazada por evento inexistente ");
    }

    @Test
    @DisplayName("Servicio 9 -> Obtener top 5 eventos")
    void obtenerTop5Eventos_Positive() {
        Usuarios asistente1 = Usuarios.builder().nombre("Ana").rol(Rol.ESTUDIANTE).build();
        usuarioRepository.save(asistente1);

        Usuarios asistente2 = Usuarios.builder().nombre("Carlos").rol(Rol.ESTUDIANTE).build();
        usuarioRepository.save(asistente2);

        Evento e1 = Evento.builder().nombre("Fiesta grande").fecha(LocalDateTime.now().plusDays(5)).creador(profesor).build();
        eventoRepository.save(e1);

        Evento e2 = Evento.builder().nombre("Taller pequeño").fecha(LocalDateTime.now().plusDays(10)).creador(profesor).build();
        eventoRepository.save(e2);

        eventosUsuariosRepository.save(EventosUsuarios.builder().idEvento(e1).idUsuario(asistente1).estado(Estado.ASISTENTE).build());
        eventosUsuariosRepository.save(EventosUsuarios.builder().idEvento(e1).idUsuario(asistente2).estado(Estado.ASISTENTE).build());

        eventosUsuariosRepository.save(EventosUsuarios.builder().idEvento(e2).idUsuario(asistente1).estado(Estado.ASISTENTE).build());

        List<TopEventoDTO> result = eventoService.obtenerTop5Eventos();

        assertFalse(result.isEmpty());
        assertEquals("Fiesta grande", result.getFirst().getNombreEvento());
        assertEquals(2L, result.getFirst().getNumAsistentes());
        System.out.println("Top 5 eventos con asistentes correcto");
    }

    @Test
    @DisplayName("Servicio 9 -> Obtener top 5 eventos negativo")
    void obtenerTop5Eventos_Negative_SinEventos() {
        eventosUsuariosRepository.deleteAll();
        eventoRepository.deleteAll();

        List<TopEventoDTO> result = eventoService.obtenerTop5Eventos();

        assertTrue(result.isEmpty());
        System.out.println("Top 5 vacío sin eventos");
    }
}