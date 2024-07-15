package com.example.time_tracker.service;

import com.example.time_tracker.model.dto.RecordDto;

import java.util.List;

public interface RecordService {
    List<RecordDto> findAll();
    List<RecordDto> findAllForCurrentUserByProjectId(Long id);
    List<RecordDto> findAllGenerally();
    List<RecordDto> findAllByUserId(Long id);
    List<RecordDto> findAllByProjectId(Long id);
    RecordDto createByProjectId(Long id, RecordDto recordDto);
    RecordDto findById(Long id);
    RecordDto updateById(Long id, RecordDto recordDto);
    void deleteById(Long id);
}
