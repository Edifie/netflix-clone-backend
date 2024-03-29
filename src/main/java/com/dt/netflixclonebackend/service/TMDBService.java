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
import com.dt.netflixclonebackend.service.dto.GenreDTO;
import com.dt.netflixclonebackend.service.dto.TMDBMovieDTO;
import com.dt.netflixclonebackend.service.mapper.ContentMapper;
import com.dt.netflixclonebackend.service.mapper.GenreMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    // --------- MOVIES ---------
    public Mono<List<JsonNode>> getMoviesFromTMDB(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenAuth);

        return webClient.get()
                .uri(endpoint)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response);
                        JsonNode resultsNode = jsonNode.get("results");
                        return Flux.just(resultsNode);
                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                })
                .collectList();
    }

    public Mono<List<JsonNode>> getAllMovies() {
        List<String> allEndpoints = generateEndpoints();

        return Flux.fromIterable(allEndpoints)
                .flatMap(this::getMoviesFromTMDB)
                .flatMapIterable(list -> list)
                .collectList();
    }

    public List<String> generateEndpoints() {
        List<String> allEndpoints = new ArrayList<>();
        String[] baseEndpoints = { "/movie/popular?language=en-US", "/movie/top_rated?language=en-US" };

        for (String baseEndpoint : baseEndpoints) {
            for (int page = 1; page <= 20; page++) {
                String endpoint = baseEndpoint + "&page=" + page;
                allEndpoints.add(endpoint);
            }
        }

        return allEndpoints;
    }

    public Mono<Void> getMoviesFromTMDBAndSave() {
        return getAllMovies()
                .doOnNext(allResults -> extractMoviesAndSave(allResults))
                .then();
    }

    public void extractMoviesAndSave(List<JsonNode> allResults) {
        List<TMDBMovieDTO> movieDTOs = new ArrayList<>();

        for (JsonNode result : allResults) {
            if (result.isArray()) {
                ArrayNode arrayNode = (ArrayNode) result;
                for (JsonNode movieNode : arrayNode) {
                    String title = movieNode.get("title").asText();
                    String releaseDateText = movieNode.get("release_date").asText();
                    LocalDate releaseDate = LocalDate.parse(releaseDateText);
                    String overview = movieNode.get("overview").asText();
                    Long movieId = movieNode.get("id").asLong();
                    String filePath = movieNode.get("poster_path").asText();

                    // Check if the movie already exists in movieDTOs to avoid duplicates in the
                    // JSON response
                    boolean movieExists = movieDTOs.stream()
                            .anyMatch(movieDTO -> movieDTO.getTitle().equals(title) &&
                                    movieDTO.getRelease_date().equals(releaseDate));

                    if (!movieExists) {
                        TMDBMovieDTO movieDTO = new TMDBMovieDTO();
                        movieDTO.setTitle(title);
                        movieDTO.setOverview(overview);
                        movieDTO.setRelease_date(releaseDate);
                        movieDTO.setMovie_id(movieId);

                        String originalFilePath = "https://image.tmdb.org/t/p/w500" + filePath;
                        movieDTO.setFile_path(originalFilePath);

                        // Set genre DTOs
                        List<Long> genreIds = new ArrayList<>();
                        for (JsonNode genreIdNode : movieNode.get("genre_ids")) {
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

                        movieDTOs.add(movieDTO);
                    }
                }
            }
        }

        if (!movieDTOs.isEmpty()) {
            List<Content> movies = new ArrayList<>();
            for (TMDBMovieDTO movieDTO : movieDTOs) {
                // Check existing movie before adding
                Content existingMovie = contentRepository.findByReleaseDateAndTitle(movieDTO.getRelease_date(),
                        movieDTO.getTitle());
                if (existingMovie == null) {
                    Content content = contentMapper.movieDtoToContent(movieDTO);
                    content.setContentType(ContentType.MOVIE);
                    movies.add(content);
                }
            }

            contentRepository.saveAll(movies);

        }
    }

    // --------- GENRES ---------
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

    // --------- IMAGES ---------
    public void generateEndpointsForImages() {
        List<Content> allContents = contentRepository.findAllByImageUrlIsNull();

        for (Content content : allContents) {

            String endpoint = "/movie/" + content.getContentId() + "/images";
            getImagesForContents(endpoint, content.getContentId());
        }

    }

    public void getImagesForContents(String endpoint, Long contentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenAuth);

        webClient.get()
                .uri(endpoint)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response);

                        JsonNode backdrops = jsonNode.get("backdrops");

                        String filePath;
                        if (!backdrops.isEmpty()) {
                            filePath = backdrops.get(0).get("file_path").asText();
                        } else {
                            JsonNode posters = jsonNode.get("posters").get(0);
                            filePath = posters.get("file_path").asText();
                        }

                        TMDBMovieDTO movieDTO = new TMDBMovieDTO();
                        movieDTO.setFile_path(filePath);

                        if (movieDTO != null) {

                            Content content = contentRepository.findByContentId(contentId);
                            // Check later how to map one field without modifying existing content
                            String originalFilePath = "https://image.tmdb.org/t/p/w500" + filePath;
                            content.setImageUrl(originalFilePath);
                            contentRepository.save(content);
                        }

                        return Flux.just(movieDTO);
                    } catch (Exception e) {
                        return Flux.error(e);
                    }
                })
                .blockFirst();
    }

}