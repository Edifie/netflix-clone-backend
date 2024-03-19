package com.dt.netflixclonebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
