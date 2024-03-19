package com.dt.netflixclonebackend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dt.netflixclonebackend.domain.Content;
import com.dt.netflixclonebackend.domain.Genre;
import com.dt.netflixclonebackend.domain.enums.ContentType;
import com.dt.netflixclonebackend.repository.ContentRepository;
import com.dt.netflixclonebackend.repository.GenreRepository;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;
import com.dt.netflixclonebackend.service.dto.GenreDTO;
import com.dt.netflixclonebackend.service.mapper.ContentMapper;
import com.dt.netflixclonebackend.service.mapper.GenreMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class TMDBService {

    private final Logger log = LoggerFactory.getLogger(TMDBService.class);
    private final WebClient webClient;

    private final ContentMapper contentMapper;
    private final GenreMapper genreMapper;

    private final ContentRepository contentRepository;
    private final GenreRepository genreRepository;

    @Value("${tmdb.api.key}")
    String accessTokenAuth;

    public TMDBService(WebClient.Builder webClientBuilder, ContentMapper contentMapper,
            ContentRepository contentRepository, GenreMapper genreMapper, GenreRepository genreRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3")
                .build();
        this.contentMapper = contentMapper;
        this.contentRepository = contentRepository;
        this.genreMapper = genreMapper;
        this.genreRepository = genreRepository;
    }

    public void getPopulerMoviesFromTMDBAndSave() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenAuth);

        String url = "/movie/top_rated?language=en-US&page=1";

        List<ContentMovieDTO> movieDTOs = webClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode resultsNode = jsonNode.get("results");

                        List<ContentMovieDTO> resultDTOs = new ArrayList<>();

                        for (JsonNode result : resultsNode) {
                            ContentMovieDTO movieDTO = new ContentMovieDTO();
                            movieDTO.setTitle(result.get("title").asText());
                            movieDTO.setOverview(result.get("overview").asText());
                            movieDTO.setRelease_date(LocalDate.parse(result.get("release_date").asText()));

                            List<Long> genreIds = new ArrayList<>();
                            for (JsonNode genreIdNode : result.get("genre_ids")) {
                                genreIds.add(genreIdNode.asLong());
                            }

                            List<GenreDTO> genresDTOs = new ArrayList<>();
                            for (Long genreId : genreIds) {
                                Genre existingGenre = genreRepository.findByCode(genreId);
                                GenreDTO existingGenreDTO = genreMapper.entityToDto(existingGenre);

                                if (existingGenre != null) {
                                    genresDTOs.add(existingGenreDTO);

                                } else {
                                    Genre newGenre = new Genre();
                                    newGenre.setCode(genreId);
                                    genreRepository.save(newGenre);

                                    GenreDTO newGenreDTO = genreMapper.entityToDto(newGenre);
                                    genresDTOs.add(newGenreDTO);
                                }
                            }
                            movieDTO.setGenreDTOs(genresDTOs);

                            resultDTOs.add(movieDTO);
                        }
                        return Flux.fromIterable(resultDTOs);
                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                })
                .collectList()
                .block();

        if (movieDTOs != null) {
            List<Content> movies = new ArrayList<>();
            for (ContentMovieDTO movieDTO : movieDTOs) {
                Content content = contentMapper.movieDtoToContent(movieDTO);
                content.setContentType(ContentType.MOVIE);
                movies.add(content);
            }

            contentRepository.saveAll(movies);
        }

    }

    public void getGenresFromTMDBAndSave() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenAuth);

        String url = "/genre/movie/list?language=en";
        List<GenreDTO> genreDTOs = webClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode resultsNode = jsonNode.get("genres");

                        List<GenreDTO> resultDTOs = new ArrayList<>();

                        for (JsonNode result : resultsNode) {
                            GenreDTO genreDTO = new GenreDTO();
                            genreDTO.setCode(result.get("id").asLong());
                            genreDTO.setName(result.get("name").asText());

                            resultDTOs.add(genreDTO);
                        }
                        return Flux.fromIterable(resultDTOs);
                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                })
                .collectList()
                .block();

        if (genreDTOs != null) {
            List<Genre> genres = new ArrayList<>();
            for (GenreDTO genreDTO : genreDTOs) {
                Genre genre = genreMapper.dtoToEntity(genreDTO);
                genres.add(genre);
            }

            genreRepository.saveAll(genres);
        }
    }
}