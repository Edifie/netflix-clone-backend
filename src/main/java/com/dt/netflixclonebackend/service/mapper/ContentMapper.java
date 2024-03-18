package com.dt.netflixclonebackend.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.dt.netflixclonebackend.domain.Content;
import com.dt.netflixclonebackend.domain.Genre;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    ContentMapper contentMapper = Mappers.getMapper(ContentMapper.class);

    @Mapping(target = "title", source = "movieDTO.title")
    @Mapping(target = "description", source = "movieDTO.overview")
    @Mapping(target = "releaseDate", source = "movieDTO.release_date")
    @Mapping(target = "genres", source = "movieDTO.genre_ids", qualifiedByName = "mapGenreIdsToGenres")
    Content movieDtoToContent(ContentMovieDTO movieDTO);

    @Named("mapGenreIdsToGenres")
    default List<Genre> mapGenreIdsToGenres(List<Long> genreIds) {
        return genreIds.stream()
                .map(genreId -> {
                    Genre genre = new Genre();
                    genre.setId(genreId);
                    return genre;
                })
                .collect(Collectors.toList());
    }
}
