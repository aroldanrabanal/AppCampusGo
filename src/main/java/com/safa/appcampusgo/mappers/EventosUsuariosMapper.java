package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.EventosUsuarios;
import com.safa.appcampusgo.dtos.EventosUsuariosDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventosUsuariosMapper {
    @Mapping(target = "usuarioId", source = "idUsuario.id")
    @Mapping(target = "usuarioNombre", source = "idUsuario.nombre")
    @Mapping(target = "eventoId", source = "idEvento.id")
    @Mapping(target = "eventoNombre", source = "idEvento.nombre")
    EventosUsuariosDTO toDTO(EventosUsuarios eventosUsuarios);

    @Mapping(target = "idUsuario.id", source = "usuarioId")
    @Mapping(target = "idEvento.id", source = "eventoId")
    EventosUsuarios toEntity(EventosUsuariosDTO dto);
}