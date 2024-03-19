package com.dt.netflixclonebackend.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.dt.netflixclonebackend.domain.Content;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    ContentMapper contentMapper = Mappers.getMapper(ContentMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "overview")
    @Mapping(target = "releaseDate", source = "release_date")
    @Mapping(target = "genres", source = "genreDTOs")
    Content movieDtoToContent(ContentMovieDTO movieDTO);

}
