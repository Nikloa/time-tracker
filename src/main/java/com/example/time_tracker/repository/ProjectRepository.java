package com.example.time_tracker.repository;

import com.example.time_tracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);
}
