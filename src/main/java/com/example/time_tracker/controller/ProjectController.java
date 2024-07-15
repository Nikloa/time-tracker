package com.example.time_tracker.controller;

import com.example.time_tracker.model.dto.ProjectDto;
import com.example.time_tracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDto>> allProjects() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/user/projects")
    public ResponseEntity<List<ProjectDto>> allProjectsByCurrentUser() {
        return ResponseEntity.ok().body(service.findAllByCurrentUser());
    }

    @GetMapping("/projects/user/{id}")
    public ResponseEntity<List<ProjectDto>> allProjectsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findAllByUserId(id));
    }

    @PostMapping("/projects/new")
    public ResponseEntity<?> creatProject(@RequestBody @Valid ProjectDto projectDto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        ProjectDto dto = service.create(projectDto);
        return ResponseEntity.created(URI.create("/projects/" + dto.getId())).body(dto);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDto projectDto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        return ResponseEntity.ok().body(service.updateById(id, projectDto));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}