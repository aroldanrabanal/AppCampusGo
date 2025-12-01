package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.dtos.EventoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventoMapper {
    @Mapping(target = "creadorId", source = "creador.id", ignore = true)
    @Mapping(target = "creadorNombre", source = "creador.nombre", ignore = true)
    EventoDTO toDTO(Evento evento);

    @Mapping(target = "creador.id", source = "creadorId", ignore = true)
    Evento toEntity(EventoDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEvento(EventoDTO dto, @MappingTarget Evento entity);
}