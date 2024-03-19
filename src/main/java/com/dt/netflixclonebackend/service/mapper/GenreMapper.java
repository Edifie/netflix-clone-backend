package com.dt.netflixclonebackend.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.dt.netflixclonebackend.domain.Genre;
import com.dt.netflixclonebackend.service.dto.GenreDTO;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper contentMapper = Mappers.getMapper(GenreMapper.class);

    @Mapping(target = "id", source = "genreDTO.id")
    @Mapping(target = "name", source = "genreDTO.name")
    Genre dtoToEntity(GenreDTO genreDTO);
}
