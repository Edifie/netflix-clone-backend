package com.dt.netflixclonebackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

    Content findByReleaseDateAndTitle(LocalDate releaseDate, String title);

    Content findByContentId(Long contentId);

    List<Content> findAllByImageUrlIsNull();

}
