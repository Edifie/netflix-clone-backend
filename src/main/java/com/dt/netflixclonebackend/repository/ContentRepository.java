package com.dt.netflixclonebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
