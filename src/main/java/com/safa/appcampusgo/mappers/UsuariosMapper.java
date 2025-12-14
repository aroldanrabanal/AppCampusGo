package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.Usuarios;
import com.safa.appcampusgo.dtos.UsuariosDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuariosMapper {

    @Mapping(target = "cursoId", source = "curso.id")
    @Mapping(target = "cursoNombre", source = "curso.nombre")
    @Mapping(target = "cursoGrupo", source = "curso.grupo")
    UsuariosDTO toDTO(Usuarios usuarios);

    @Mapping(target = "curso.id", source = "cursoId")
    Usuarios toEntity(UsuariosDTO dto);
}