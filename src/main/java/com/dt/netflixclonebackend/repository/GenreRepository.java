package com.dt.netflixclonebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByCodeIn(List<Long> codes);

    Genre findByCode(Long code);

}
