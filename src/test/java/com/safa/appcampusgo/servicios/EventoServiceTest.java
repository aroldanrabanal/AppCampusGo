package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.dtos.EventoSimpleDTO;
import com.safa.appcampusgo.dtos.EventosUsuariosDTO;
import com.safa.appcampusgo.dtos.GaleriaDTO;
import com.safa.appcampusgo.modelos.Estado;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
        assertEquals(profesor.getId(), result.getCreadorId());  // Verifica creador
        System.out.println("Nuevo evento creado correctamente ✓");
    }

    @Test
    void crearEvento_Negative_CreadorNoExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Evento inválido");
        dto.setFecha(LocalDateTime.now().plusDays(5));

        assertThrows(NoSuchElementException.class,
                () -> eventoService.crearEvento(dto, 999),
                "Debe lanzar excepción si el creador no existe en BD");
        System.out.println("Creación rechazada por creador inexistente ✓");
    }

    @Test
    void listarEventosFiltrados_Positive() {
        Page<EventoDTO> result = eventoService.listarEventosFiltrados(
                null, "Fiesta", "SAFA", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals("Fiesta fin de curso", result.getContent().get(0).getNombre());
        System.out.println("Listado de eventos con filtros correcto ✓");
    }

    @Test
    void listarEventosFiltrados_Negative_SinResultados() {
        Page<EventoDTO> result = eventoService.listarEventosFiltrados(
                LocalDateTime.now().plusYears(1), "Inexistente", "SAFA", PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
        System.out.println("No resultados con filtros inválidos ✓");
    }

    @Test
    void obtenerEventoPorId_Positive() {
        Optional<EventoDTO> result = eventoService.obtenerEventoPorId(eventoComun.getId());

        assertTrue(result.isPresent());
        assertEquals("Fiesta fin de curso", result.get().getNombre());
        System.out.println("Detalle de evento obtenido ✓");
    }

    @Test
    void obtenerEventoPorId_Negative_NoExiste() {
        Optional<EventoDTO> result = eventoService.obtenerEventoPorId(999);

        assertFalse(result.isPresent());
        System.out.println("Evento inexistente devuelve vacío ✓");
    }

    @Test
    void modificarEvento_Positive() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Fiesta fin de curso - ACTUALIZADA");
        dto.setDescripcion("Nueva descripción");

        EventoDTO result = eventoService.modificarEvento(eventoComun.getId(), dto);

        assertEquals("Fiesta fin de curso - ACTUALIZADA", result.getNombre());
        System.out.println("Evento modificado correctamente ✓");
    }

    @Test
    void modificarEvento_Negative_NoExiste() {
        EventoDTO dto = new EventoDTO();
        dto.setNombre("Intento fallido");

        assertThrows(Exception.class,  // Puede ser NoSuchElementException
                () -> eventoService.modificarEvento(999, dto),
                "Debe fallar si no existe");
        System.out.println("Modificación rechazada por ID inexistente ✓");
    }

    @Test
    void inscribirUsuario_Positive() {
        EventosUsuariosDTO result = eventosUsuariosService.inscribirUsuario(
                profesor.getId(), eventoComun.getId(), Estado.ASISTENTE);

        assertNotNull(result.getId());
        assertEquals(Estado.ASISTENTE, result.getEstado());
        System.out.println("Inscripción como ASISTENTE realizada ✓");
    }

    @Test
    void inscribirUsuario_Negative_YaInscrito() {
        eventosUsuariosService.inscribirUsuario(profesor.getId(), eventoComun.getId(), Estado.INTERESADO);

        assertThrows(IllegalArgumentException.class,
                () -> eventosUsuariosService.inscribirUsuario(profesor.getId(), eventoComun.getId(), Estado.ASISTENTE),
                "Ya inscrito");
        System.out.println("Inscripción duplicada rechazada ✓");
    }

    @Test
    void subirMultimedia_Positive() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto-fiesta.jpg", "image/jpeg", "foto de prueba".getBytes());

        GaleriaDTO result = galeriaService.subirMultimedia(eventoComun.getId(), file, "Foto de la fiesta");

        assertNotNull(result.getId());
        assertTrue(result.getUrl().contains("/uploads/"));
        assertEquals("Foto de la fiesta", result.getDescripcion());
        System.out.println("Foto subida correctamente al evento ✓");
    }

    @Test
    void subirMultimedia_Negative_EventoNoExiste() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "foto-invalida.jpg", "image/jpeg", "contenido".getBytes());

        assertThrows(Exception.class,
                () -> galeriaService.subirMultimedia(999, file, "Foto inválida"),
                "Debe fallar si el evento no existe");
        System.out.println("Subida rechazada por evento inexistente ✓");
    }
}