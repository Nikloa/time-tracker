package com.example.time_tracker.service.impl;

import com.example.time_tracker.model.Project;
import com.example.time_tracker.model.User;
import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.repository.ProjectRepository;
import com.example.time_tracker.repository.UserRepository;
import com.example.time_tracker.service.UserService;
import com.example.time_tracker.util.convertor.RequestMapper;
import com.example.time_tracker.util.convertor.UserMapper;
import com.example.time_tracker.model.dto.UserDto;
import com.example.time_tracker.util.exception.ModelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;
    private final RequestMapper requestMapper;

//    Возвращает список всех пользователей
    @Override
    public List<UserDto> findAll() {
        return userMapper.toListDto(userRepository.findAll());
    }

//    Возвращает пользователя по id, если id указан неверно, выбрасывается исключение
    @Override
    public UserDto findById(Long id) {
        return userMapper.modelToDto(
                userRepository.findById(id).orElseThrow(
                        () -> new ModelNotFoundException("User with id: " + id + " not found")));
    }

//    Используется текущим пользователем для получения своих же данных
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserDto findCurrentUser() {
        return userMapper.modelToDto(userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
                () -> new UsernameNotFoundException("Authenticated user not found")));
    }

//    Возвращает список пользователей назначенных на проект с id,
//    если id указан неверно выбрасывается исключение
    @Override
    public List<UserDto> findAllByProjectId(Long id) {
        return userMapper.toListDto(projectRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("Project with id: " + id + " not found")).getUsers());
    }

//    Обновление пользователя по id, если id указан неверно выбрасывается исключение
    @Override
    @Transactional
    public UserDto updateById(Long id, RegistrationRequest request) {
        if (userRepository.existsById(id)) {
            User user = requestMapper.requestToModel(request);
            user.setId(id);
            return userMapper.modelToDto(userRepository.save(user));
        } else {
            throw new ModelNotFoundException("User with id: " + id + " not found");
        }
    }

//    Удаление пользователя по id из базы и у назначенных ему проектов,
//    если id указан неверно выбрасывается исключение
    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                        () -> new ModelNotFoundException("User with id: " + id + " not found"));
        List<Project> projects = user.getProjects();
        projects.forEach(p -> p.getUsers().remove(user));
        userRepository.delete(user);
    }

//    Назначение пользователю с userId проекта с projectId,
//    если id указаны неверно выбрасывается исключение
    @Override
    @Transactional
    public void createUserProjectRelation(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException("User with id: " + userId + " not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ModelNotFoundException("Project with id: " + projectId + " not found"));

        user.getProjects().add(project);
        project.getUsers().add(user);
    }

//    Удаление пользователя с userId из проекта с projectId,
//    если id указаны неверно выбрасывается исключение
    @Override
    @Transactional
    public void deleteUserProjectRelation(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException("User with id: " + userId + " not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ModelNotFoundException("Project with id: " + projectId + " not found"));

        user.getProjects().stream().filter(r -> r.getId().equals(projectId)).findFirst().orElseThrow(
                () -> new ModelNotFoundException("User with id=" + userId +" has no relation to project with id=" + projectId));

        user.getProjects().remove(project);
        project.getUsers().remove(user);
    }
}
