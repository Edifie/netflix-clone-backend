package com.dt.netflixclonebackend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.dt.netflixclonebackend.domain.User;
import com.dt.netflixclonebackend.service.UserService;
import com.dt.netflixclonebackend.service.dto.UserDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value = "/user/")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("createAccount")
    public ResponseEntity<User> createAccount(@RequestBody UserDTO userDTO) {
        User user = this.userService.createAccount(userDTO);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
