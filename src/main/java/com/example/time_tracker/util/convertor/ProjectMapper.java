package com.example.time_tracker.util.convertor;

import com.example.time_tracker.model.Project;
import com.example.time_tracker.model.dto.ProjectDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project dtoToModel(ProjectDto dto);
    ProjectDto modelToDto(Project model);
    List<ProjectDto> toListDto(List<Project> models);
}
