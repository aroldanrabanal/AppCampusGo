package com.safa.appcampusgo.mappers;

import com.safa.appcampusgo.modelos.Cursos;
import com.safa.appcampusgo.dtos.CursosDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CursosMapper {
    CursosDTO toDTO(Cursos cursos);

    Cursos toEntity(CursosDTO dto);
}