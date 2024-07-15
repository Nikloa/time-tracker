package com.example.time_tracker.service.impl;

import com.example.time_tracker.model.Project;
import com.example.time_tracker.model.User;
import com.example.time_tracker.model.dto.ProjectDto;
import com.example.time_tracker.repository.ProjectRepository;
import com.example.time_tracker.repository.UserRepository;
import com.example.time_tracker.service.ProjectService;
import com.example.time_tracker.util.convertor.ProjectMapper;
import com.example.time_tracker.util.exception.ModelNotFoundException;
import com.example.time_tracker.util.exception.ProjectNameAlreadyExistException;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

//    Метод возвращает список проектов
    @Override
    public List<ProjectDto> findAll() {
        return projectMapper.toListDto(projectRepository.findAll());
    }

//    Метод возвращает список всех проектов назначенных текущему пользователю
    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ProjectDto> findAllByCurrentUser() {
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(
                () -> new UsernameNotFoundException("Authenticated user not found"));
        return projectMapper.toListDto(user.getProjects());
    }

//    Метод возвращает список проектов назначенных пользователю с id
//    Если такого пользователя не найлено выбрасывается исключение
    @Override
    public List<ProjectDto> findAllByUserId(Long id) {
        return projectMapper.toListDto(userRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("User with id: " + id + " not found")).getProjects());
    }

//    Метод возвращает проект по его id
    @Override
    public ProjectDto findById(Long id) {
        return projectMapper.modelToDto(
                projectRepository.findById(id).orElseThrow(
                        () -> new ModelNotFoundException("Project with id: " + id + " not found")));
    }

//    Метод сохраняет новый проект в базу
//    Если название проекта не уникально выбрасывается исключение
    @Override
    @Transactional
    public ProjectDto create(ProjectDto projectDto) {
        if (projectRepository.existsByName(projectDto.getName())) {
            throw new ProjectNameAlreadyExistException("Project with name " + projectDto.getName() + " already exist");
        }

        return projectMapper.modelToDto(projectRepository.save(
                projectMapper.dtoToModel(projectDto)));
    }

//    Метод обновляет проект, если id указан неверно выбрасывается исключение
    @Override
    @Transactional
    public ProjectDto updateById(Long id, ProjectDto projectDto) {
        if (projectRepository.existsById(id)) {
            Project project = projectMapper.dtoToModel(projectDto);
            project.setId(id);
            return projectMapper.modelToDto(projectRepository.save(project));
        } else {
            throw new ModelNotFoundException("Project with id: " + id + " not found");
        }
    }

//    Метод удаляет проект из базы, а так же у пользователей которым он назначен,
//    если id указан неверно выбрасывается исключение
    @Override
    @Transactional
    public void deleteById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException("Project with id: " + id + " not found"));
        List<User> users = project.getUsers();
        users.forEach((u -> u.getProjects().remove(project)));
        projectRepository.delete(project);
    }
}
