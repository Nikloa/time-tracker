package com.example.time_tracker.service;

import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto findCurrentUser();
    UserDto findById(Long id);
    List<UserDto> findAllByProjectId(Long id);
    UserDto updateById(Long id, RegistrationRequest request);
    void deleteById(Long id);
    void createUserProjectRelation(Long userId, Long projectId);
    void deleteUserProjectRelation(Long userId, Long projectId);
}
