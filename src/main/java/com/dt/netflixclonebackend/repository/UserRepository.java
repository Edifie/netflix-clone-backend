package com.dt.netflixclonebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dt.netflixclonebackend.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
