package com.dt.netflixclonebackend.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

    Content findByReleaseDateAndTitle(LocalDate releaseDate, String title);

}
