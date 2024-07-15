package com.example.time_tracker.service;

import com.example.time_tracker.model.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> findAll();
    List<ProjectDto> findAllByCurrentUser();
    List<ProjectDto> findAllByUserId(Long id);
    ProjectDto create(ProjectDto projectDto);
    ProjectDto findById(Long id);
    ProjectDto updateById(Long id, ProjectDto projectDto);
    void deleteById(Long id);
}
