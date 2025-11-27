package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.modelos.Galeria;
import com.safa.appcampusgo.servicios.GaleriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/eventos/{eventoId}/galeria")
@AllArgsConstructor
public class GaleriaController {

    private final GaleriaService galeriaService;

    // Subir multimedia (endpoint 7: usa Multipart para file).
    @PostMapping
    public ResponseEntity<Galeria> subirMultimedia(@PathVariable Integer eventoId,
                                                   @RequestPart("file") MultipartFile file,
                                                   @RequestParam(required = false) String descripcion) throws IOException {
        Galeria subida = galeriaService.subirMultimedia(eventoId, file, descripcion);
        return new ResponseEntity<>(subida, HttpStatus.CREATED);
    }

    // Lista por evento.
    @GetMapping
    public List<Galeria> obtenerGaleria(@PathVariable Integer eventoId) {
        return galeriaService.obtenerGaleriaPorEvento(eventoId);
    }
}