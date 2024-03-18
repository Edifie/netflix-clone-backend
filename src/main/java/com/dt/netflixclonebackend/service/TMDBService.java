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
import com.dt.netflixclonebackend.domain.enums.ContentType;
import com.dt.netflixclonebackend.repository.ContentRepository;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;
import com.dt.netflixclonebackend.service.mapper.ContentMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class TMDBService {

    private final Logger log = LoggerFactory.getLogger(TMDBService.class);
    private final WebClient webClient;
    private final ContentMapper contentMapper;
    private final ContentRepository contentRepository;

    @Value("${tmdb.api.key}")
    String accessTokenAuth;

    public TMDBService(WebClient.Builder webClientBuilder, ContentMapper contentMapper,
            ContentRepository contentRepository) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3")
                .build();
        this.contentMapper = contentMapper;
        this.contentRepository = contentRepository;
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
                            for (JsonNode genreId : result.get("genre_ids")) {
                                genreIds.add(genreId.asLong());
                            }
                            movieDTO.setGenre_ids(genreIds);

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
}