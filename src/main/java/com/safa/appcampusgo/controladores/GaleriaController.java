package com.safa.appcampusgo.controladores;

import com.safa.appcampusgo.dtos.GaleriaDTO;
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

    @PostMapping
    public ResponseEntity<GaleriaDTO> subirMultimedia(@PathVariable Integer eventoId,
                                                      @RequestPart("file") MultipartFile file,
                                                      @RequestParam(required = false) String descripcion) throws IOException {
        return new ResponseEntity<>(galeriaService.subirMultimedia(eventoId, file, descripcion), HttpStatus.CREATED);
    }

    @GetMapping
    public List<GaleriaDTO> obtenerGaleria(@PathVariable Integer eventoId) {
        return galeriaService.obtenerGaleriaPorEvento(eventoId);
    }
}