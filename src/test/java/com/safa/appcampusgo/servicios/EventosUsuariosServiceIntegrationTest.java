package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.EventosUsuariosDTO;
import com.safa.appcampusgo.mappers.EventosUsuariosMapper;
import com.safa.appcampusgo.modelos.Estado;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.EventosUsuariosRepository;
import com.safa.appcampusgo.repositorios.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventosUsuariosServiceIntegrationTest {

    @InjectMocks
    private EventosUsuariosService eventosUsuariosService;

    @Mock
    private EventosUsuariosRepository eventosUsuariosRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private EventosUsuariosMapper eventosUsuariosMapper;

    @Test
    @DisplayName("Servicio 6 -> Inscribir usuario")
    void inscribirUsuario() {
        Usuarios usuario = Usuarios.builder()
                .id(1)
                .nombre("Angel")
                .build();

        Evento evento = Evento.builder()
                .id(1)
                .nombre("Evento Test")
                .build();

        EventosUsuarios inscripcion = EventosUsuarios.builder()
                .id(1)
                .idUsuario(usuario)
                .idEvento(evento)
                .estado(Estado.ASISTENTE)
                .build();

        EventosUsuariosDTO dto = new EventosUsuariosDTO();
        dto.setId(1);
        dto.setEstado(Estado.ASISTENTE);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(eventoRepository.findById(1)).thenReturn(Optional.of(evento));
        when(eventosUsuariosRepository.findByIdUsuarioIdAndIdEventoId(1, 1))
                .thenReturn(Optional.empty());
        when(eventosUsuariosRepository.save(any(EventosUsuarios.class))).thenReturn(inscripcion);
        when(eventosUsuariosMapper.toDTO(inscripcion)).thenReturn(dto);

        EventosUsuariosDTO resultado = eventosUsuariosService.inscribirUsuario(1, 1, Estado.ASISTENTE);

        assertNotNull(resultado);
        assertEquals(Estado.ASISTENTE, resultado.getEstado());
        verify(usuarioRepository).findById(1);
        verify(eventoRepository).findById(1);
        verify(eventosUsuariosRepository).save(any(EventosUsuarios.class));
    }
}
