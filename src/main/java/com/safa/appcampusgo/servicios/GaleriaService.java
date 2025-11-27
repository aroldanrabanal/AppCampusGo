package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Galeria;
import com.safa.appcampusgo.repositorios.GaleriaRepository;
import com.safa.appcampusgo.repositorios.EventoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GaleriaService {
    private final GaleriaRepository galeriaRepository;
    private final EventoRepository eventoRepository;

    // Subir foto/resultados (endpoint 7: maneja MultipartFile en controlador, guarda URL).
    public Galeria subirMultimedia(Integer eventoId, MultipartFile file, String descripcion) throws IOException {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        // Lógica de storage: Guarda archivo localmente
        String uploadDir = "uploads/" + eventoId;
        new File(uploadDir).mkdirs();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadDir + "/" + fileName));
        String url = "/uploads/" + eventoId + "/" + fileName;  // URL relativa

        Galeria galeria = Galeria.builder()
                .evento(evento)
                .url(url)
                .descripcion(descripcion)
                .build();
        return galeriaRepository.save(galeria);
    }

    // Lista por evento (para vista en Ionic).
    public List<Galeria> obtenerGaleriaPorEvento(Integer eventoId) {
        return galeriaRepository.findByEventoId(eventoId);
    }
}