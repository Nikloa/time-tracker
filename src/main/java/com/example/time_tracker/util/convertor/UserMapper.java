package com.example.time_tracker.util.convertor;

import com.example.time_tracker.model.User;
import com.example.time_tracker.model.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToModel(UserDto dto);
    UserDto modelToDto(User model);
    List<UserDto> toListDto(List<User> models);
}
