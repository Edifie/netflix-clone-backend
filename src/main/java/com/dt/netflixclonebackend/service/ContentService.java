package com.dt.netflixclonebackend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dt.netflixclonebackend.domain.Content;
import com.dt.netflixclonebackend.repository.ContentRepository;
import com.dt.netflixclonebackend.service.dto.ContentMovieDTO;
import com.dt.netflixclonebackend.service.mapper.ContentMapper;

@Service
public class ContentService {

    private final Logger log = LoggerFactory.getLogger(ContentService.class);

    private ContentRepository contentRepository;
    private ContentMapper contentMapper;

    public ContentService(ContentRepository contentRepository, ContentMapper contentMapper) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
    }

    public List<ContentMovieDTO> getMovieContents() {

        List<Content> contents = contentRepository.findAll();
        return contents.stream()
                .map(contentMapper::lightMovieEntityToDto)
                .collect(Collectors.toList());
    }

}
