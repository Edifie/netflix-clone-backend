package com.dt.netflixclonebackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dt.netflixclonebackend.service.TMDBService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/content/")
public class ContentController {
    private final Logger log = LoggerFactory.getLogger(TMDBService.class);

    private final TMDBService tmdbService;

    public ContentController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("movie/all")
    public ResponseEntity<?> getFetchedMoviesFromTMDB() {
        tmdbService.getPopulerMoviesFromTMDBAndSave();

        return ResponseEntity.status(HttpStatus.OK).body("Movies fetched and saved successfully");
    }

}
