package com.dt.netflixclonebackend.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.dt.netflixclonebackend.domain.Content;
import com.dt.netflixclonebackend.domain.Genre;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;
import com.dt.netflixclonebackend.service.dto.GenreDTO;
import com.dt.netflixclonebackend.service.dto.TMDBMovieDTO;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    ContentMapper contentMapper = Mappers.getMapper(ContentMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "overview")
    @Mapping(target = "releaseDate", source = "release_date")
    @Mapping(target = "contentId", source = "movie_id")
    @Mapping(target = "genres", source = "genreDTOs")
    Content movieDtoToContent(TMDBMovieDTO dto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "genreDTOs", expression = "java(mapGenres(entity.getGenres()))")
    ContentMovieDTO lightMovieEntityToDto(Content entity);

    default List<GenreDTO> mapGenres(List<Genre> genres) {
        return genres.stream()
                .map(genre -> {
                    GenreDTO genreDTO = new GenreDTO();
                    genreDTO.setName(genre.getName());
                    genreDTO.setCode(genre.getCode());
                    return genreDTO;
                })
                .collect(Collectors.toList());
    }

}
