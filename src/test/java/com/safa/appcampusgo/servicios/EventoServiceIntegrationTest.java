package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventoDTO;
import com.safa.appcampusgo.dtos.TopEventoDTO;
import com.safa.appcampusgo.mappers.EventoMapper;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Rol;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventoServiceIntegrationTest {

    @InjectMocks
    private EventoService eventoService;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EventoMapper eventoMapper;

    @Test
    @DisplayName("Servicio 2 -> Integración: Registrar evento cuando creador existe")
    void registrarEvento() {
        // Preparar datos: Creador (Usuario)
        Usuarios creador = Usuarios.builder()
                .id(1)
                .nombre("Angel")
                .apellidos("Roldán")
                .email("aaa@aaa.com")
                .rol(Rol.ESTUDIANTE)
                .build();

        // Preparar DTO de entrada
        EventoDTO dtoEvento = new EventoDTO();
        dtoEvento.setNombre("Evento de prueba");
        dtoEvento.setDescripcion("Descripción de prueba");
        dtoEvento.setFecha(LocalDateTime.now().plusDays(5));
        dtoEvento.setPrecio(0f);
        dtoEvento.setLugar("Colegio SAFA");
        dtoEvento.setCategoria("Escolar");
        dtoEvento.setInstitucion("SAFA");
        dtoEvento.setCreadorId(1);

        // Preparar entity esperado (después de mapeo y setCreador)
        Evento entity = Evento.builder()
                .nombre(dtoEvento.getNombre())
                .descripcion(dtoEvento.getDescripcion())
                .fecha(dtoEvento.getFecha())
                .precio(dtoEvento.getPrecio())
                .lugar(dtoEvento.getLugar())
                .categoria(dtoEvento.getCategoria())
                .institucion(dtoEvento.getInstitucion())
                .creador(creador)
                .build();

        // Entity salvado (con ID simulado)
        Evento savedEntity = Evento.builder()
                .id(1)
                .nombre(dtoEvento.getNombre())
                .descripcion(dtoEvento.getDescripcion())
                .fecha(dtoEvento.getFecha())
                .precio(dtoEvento.getPrecio())
                .lugar(dtoEvento.getLugar())
                .categoria(dtoEvento.getCategoria())
                .institucion(dtoEvento.getInstitucion())
                .creador(creador)
                .build();

        // Mockear dependencias
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(creador));  // Creador existe
        when(eventoMapper.toEntity(dtoEvento)).thenReturn(entity);  // Mapper crea entity base
        when(eventoRepository.save(any(Evento.class))).thenReturn(savedEntity);  // Save retorna entity con ID
        when(eventoMapper.toDTO(savedEntity)).thenReturn(dtoEvento);  // Mapper de vuelta a DTO

        // Ejecutar el método
        EventoDTO resultado = eventoService.crearEvento(dtoEvento, 1);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals("Evento de prueba", resultado.getNombre());  // Verifica salida básica
        assertEquals(1, resultado.getCreadorId());  // Verifica creador

        verify(usuarioRepository).findById(1);  // Se llamó a buscar creador
        verify(eventoMapper).toEntity(dtoEvento);  // Se llamó al mapper toEntity
        verify(eventoRepository).save(any(Evento.class));  // Se llamó a save
        verify(eventoMapper).toDTO(savedEntity);  // Se llamó al mapper toDTO
    }

    @Test
    @DisplayName("Servicio 3 -> Listar eventos ")
    void listarEventosFiltrados() {
        Evento evento = new Evento();
        evento.setId(1);
        evento.setNombre("Evento Escolar Futuro");
        evento.setFecha(LocalDateTime.now().plusDays(10));
        evento.setCategoria("Escolar");
        evento.setInstitucion("SAFA");

        Page<Evento> pageSimulada = new PageImpl<>(Collections.singletonList(evento));

        EventoDTO dto = new EventoDTO();
        dto.setId(1);
        dto.setNombre("Evento Escolar Futuro");

        // Filtros de prueba
        LocalDateTime fechaFiltro = LocalDateTime.now();
        String categoriaFiltro = "Escolar";
        String institucionFiltro = "SAFA";
        Pageable pageable = PageRequest.of(0, 10);

        // Mockear repository y mapper
        when(eventoRepository.findByFiltros(fechaFiltro, categoriaFiltro, institucionFiltro, pageable))
                .thenReturn(pageSimulada);
        when(eventoMapper.toDTO(any(Evento.class))).thenReturn(dto);

        // Ejecutar método
        Page<EventoDTO> resultado = eventoService.listarEventosFiltrados(fechaFiltro, categoriaFiltro, institucionFiltro, pageable);

        // Verificaciones
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Evento Escolar Futuro", resultado.getContent().getFirst().getNombre());
    }

    @Test
    @DisplayName("Servicio 4 -> Buscar por ID")
    void buscarPorId(){
        when(eventoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Evento()));
        when(eventoMapper.toDTO(Mockito.any(Evento.class))).thenReturn(new EventoDTO());

        eventoService.obtenerEventoPorId(1);

        verify(eventoRepository).findById(Mockito.anyInt());
        verify(eventoMapper).toDTO(Mockito.any());
    }

    @Test
    @DisplayName("Servicio 5 -> Modificar evento")
    void modificarEvento() {
        Evento eventoExistente = Evento.builder()
                .id(1)
                .nombre("Evento Original")
                .descripcion("Descripción original")
                .build();

        EventoDTO dtoModificado = new EventoDTO();
        dtoModificado.setNombre("Evento Modificado");
        dtoModificado.setDescripcion("Nueva descripción");

        Evento eventoActualizado = Evento.builder()
                .id(1)
                .nombre("Evento Modificado")
                .descripcion("Nueva descripción")
                .build();

        when(eventoRepository.findById(1)).thenReturn(Optional.of(eventoExistente));
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoActualizado);
        when(eventoMapper.toDTO(eventoActualizado)).thenReturn(dtoModificado);

        EventoDTO resultado = eventoService.modificarEvento(1, dtoModificado);

        assertNotNull(resultado);
        assertEquals("Evento Modificado", resultado.getNombre());
        verify(eventoRepository).findById(1);
        verify(eventoRepository).save(any(Evento.class));
    }



    @Test
    @DisplayName("Servicio 9 -> Top 5 eventos")
    void obtenerTop5Eventos() {
        Object[] resultado = new Object[]{"Evento Popular", 10L, "Ana, Carlos"};

        when(eventoRepository.findTop5EventosConAsistentes())
                .thenReturn(Collections.singletonList(resultado));

        List<TopEventoDTO> top = eventoService.obtenerTop5Eventos();

        assertFalse(top.isEmpty());
        assertEquals("Evento Popular", top.getFirst().getNombreEvento());
        assertEquals(10L, top.getFirst().getNumAsistentes());
        verify(eventoRepository).findTop5EventosConAsistentes();
    }
}