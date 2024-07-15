package com.example.time_tracker.controller;

import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.service.UserService;
import com.example.time_tracker.model.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> allUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/users/project/{id}")
    public ResponseEntity<List<UserDto>> allUsersByProjectId(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findAllByProjectId(id));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok().body(userService.findCurrentUser());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid RegistrationRequest request, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        return ResponseEntity.ok().body(userService.updateById(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<?> createUserProjectRelation(@PathVariable Long userId, @PathVariable Long projectId) {
        userService.createUserProjectRelation(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<?> deleteUserProjectRelation(@PathVariable Long userId, @PathVariable Long projectId) {
        userService.deleteUserProjectRelation(userId, projectId);
        return ResponseEntity.ok().build();
    }
}
