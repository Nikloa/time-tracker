package com.example.time_tracker.controller;

import com.example.time_tracker.model.dto.RecordDto;
import com.example.time_tracker.service.RecordService;
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
public class RecordController {

    private final RecordService service;

    @GetMapping("/user/records")
    public ResponseEntity<List<RecordDto>> allRecords() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/user/records/project/{id}")
    public ResponseEntity<List<RecordDto>> allRecordsForCurrentUserByProjectId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findAllForCurrentUserByProjectId(id));
    }

    @GetMapping("/admin/records")
    public ResponseEntity<List<RecordDto>> allRecordsGenerally() {
        return ResponseEntity.ok().body(service.findAllGenerally());
    }

    @GetMapping("/admin/records/user/{id}")
    public ResponseEntity<List<RecordDto>> allRecordsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findAllByUserId(id));
    }

    @GetMapping("/admin/records/project/{id}")
    public ResponseEntity<List<RecordDto>> allRecordsByProjectId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findAllByProjectId(id));
    }

    @PostMapping("/user/records/new/project/{id}")
    public ResponseEntity<?> createRecord(@PathVariable Long id, @RequestBody @Valid RecordDto recordDto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        RecordDto dto = service.createByProjectId(id, recordDto);
        return ResponseEntity.created(URI.create("/user/records/" )).body(dto);
    }

    @GetMapping("user/records/{id}")
    public ResponseEntity<RecordDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PutMapping("user/records/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable Long id, @RequestBody @Valid RecordDto recordDto, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        return ResponseEntity.ok().body(service.updateById(id, recordDto));
    }

    @DeleteMapping("user/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
