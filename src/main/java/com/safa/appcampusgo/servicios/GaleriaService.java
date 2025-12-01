package com.safa.appcampusgo.servicios;

import com.safa.appcampusgo.dtos.GaleriaDTO;
import com.safa.appcampusgo.mappers.GaleriaMapper;
import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.modelos.Galeria;
import com.safa.appcampusgo.repositorios.EventoRepository;
import com.safa.appcampusgo.repositorios.GaleriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GaleriaService {
    private final GaleriaRepository galeriaRepository;
    private final EventoRepository eventoRepository;
    private final GaleriaMapper galeriaMapper;

    public GaleriaDTO subirMultimedia(Integer eventoId, MultipartFile file, String descripcion) throws IOException {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow();
        String uploadDir = "uploads/" + eventoId;
        new File(uploadDir).mkdirs();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadDir + "/" + fileName));
        String url = "/uploads/" + eventoId + "/" + fileName;
        Galeria entity = Galeria.builder()
                .evento(evento)
                .url(url)
                .descripcion(descripcion)
                .build();
        return galeriaMapper.toDTO(galeriaRepository.save(entity));
    }

    public List<GaleriaDTO> obtenerGaleriaPorEvento(Integer eventoId) {
        return galeriaRepository.findByEventoId(eventoId).stream()
                .map(galeriaMapper::toDTO)
                .collect(Collectors.toList());
    }
}