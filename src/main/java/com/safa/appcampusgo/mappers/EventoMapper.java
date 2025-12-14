package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.Evento;
import com.safa.appcampusgo.dtos.EventoDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventoMapper {
    @Mapping(target = "creadorId", source = "creador.id")
    @Mapping(target = "creadorNombre", source = "creador.nombre")
    EventoDTO toDTO(Evento evento);

    @Mapping(target = "creador.id", source = "creadorId")
    Evento toEntity(EventoDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEvento(EventoDTO dto, @MappingTarget Evento entity);
}