package com.dt.netflixclonebackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dt.netflixclonebackend.service.TMDBService;

@RestController
@RequestMapping(value = "/tmdb/")
public class TMDBController {
    private final Logger log = LoggerFactory.getLogger(TMDBController.class);

    private final TMDBService tmdbService;

    public TMDBController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("movie/all")
    public ResponseEntity<?> getFetchedMoviesFromTMDB() {
        for (int i = 1; i <= 25; i++) {
            tmdbService.getMoviesFromTMDBAndSave("/movie/popular?language=en-US&page=" + i);
            tmdbService.getMoviesFromTMDBAndSave("/movie/top_rated?language=en-US&page=" + i);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Movies fetched and saved successfully");
    }

    @GetMapping("genre/all")
    public ResponseEntity<?> getFetchedGenresFromTMDB() {
        tmdbService.getGenresFromTMDBAndSave();

        return ResponseEntity.status(HttpStatus.OK).body("Genres fetched and saved successfully");
    }

}