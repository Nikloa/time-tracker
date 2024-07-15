package com.example.time_tracker.repository;

import com.example.time_tracker.model.Project;
import com.example.time_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.time_tracker.model.Record;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserAndProject(User user, Project project);
}
