package com.dt.netflixclonebackend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dt.netflixclonebackend.service.ContentService;
import com.dt.netflixclonebackend.service.TMDBService;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;

@RestController
@RequestMapping(value = "/content/")
public class ContentController {
    private final Logger log = LoggerFactory.getLogger(TMDBService.class);
    ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("movie/all")
    public ResponseEntity<List<ContentMovieDTO>> getFetchedGenresFromTMDB() {
        List<ContentMovieDTO> contents = contentService.getMovieContents();

        return ResponseEntity.status(HttpStatus.OK).body(contents);
    }

}
