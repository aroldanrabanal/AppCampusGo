package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.Galeria;
import com.safa.appcampusgo.dtos.GaleriaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GaleriaMapper {
    @Mapping(target = "eventoId", source = "evento.id")
    GaleriaDTO toDTO(Galeria galeria);

    @Mapping(target = "evento.id", source = "eventoId")
    Galeria toEntity(GaleriaDTO dto);
}