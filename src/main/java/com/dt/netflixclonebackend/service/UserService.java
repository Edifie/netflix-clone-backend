package com.dt.netflixclonebackend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dt.netflixclonebackend.domain.User;
import com.dt.netflixclonebackend.repository.UserRepository;
import com.dt.netflixclonebackend.service.dto.UserDTO;
import com.dt.netflixclonebackend.service.mapper.UserMapper;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User createAccount(UserDTO userDTO) {
        User user = new User();

        user = userMapper.DtoToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }
}
