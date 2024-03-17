package com.dt.netflixclonebackend.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.dt.netflixclonebackend.domain.User;
import com.dt.netflixclonebackend.service.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDTO entityToDto(User user);

    User DtoToEntity(UserDTO userDTO);
}
