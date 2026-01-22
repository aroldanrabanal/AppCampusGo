package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.GaleriaDTO;
import com.safa.appcampusgo.mappers.GaleriaMapper;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Galeria;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.GaleriaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GaleriaServiceIntegrationTest {

    @InjectMocks
    private GaleriaService galeriaService;

    @Mock
    private GaleriaRepository galeriaRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private GaleriaMapper galeriaMapper;

    @Test
    @DisplayName("Servicio 7 -> Subir foto")
    void subirFoto() throws IOException {
        Evento evento = Evento.builder()
                .id(1)
                .nombre("Evento Test")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file", "foto.jpg", "image/jpeg", "contenido".getBytes());

        Galeria galeria = Galeria.builder()
                .id(1)
                .url("/uploads/1/foto.jpg")
                .descripcion("Foto de prueba")
                .evento(evento)
                .build();

        GaleriaDTO dto = new GaleriaDTO();
        dto.setId(1);
        dto.setUrl("/uploads/1/foto.jpg");
        dto.setDescripcion("Foto de prueba");

        when(eventoRepository.findById(1)).thenReturn(Optional.of(evento));
        when(galeriaRepository.save(any(Galeria.class))).thenReturn(galeria);
        when(galeriaMapper.toDTO(any(Galeria.class))).thenReturn(dto);

        GaleriaDTO resultado = galeriaService.subirMultimedia(1, file, "Foto de prueba");

        assertNotNull(resultado);
        assertTrue(resultado.getUrl().contains("/uploads/"));
        verify(eventoRepository).findById(1);
        verify(galeriaRepository).save(any(Galeria.class));
    }
}
