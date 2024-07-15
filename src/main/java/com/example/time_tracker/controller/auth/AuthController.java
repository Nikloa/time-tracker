package com.example.time_tracker.controller.auth;

import com.example.time_tracker.model.auth.AuthRequest;
import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.model.dto.UserDto;
import com.example.time_tracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid RegistrationRequest request, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        UserDto userDto = service.create(request);
        return ResponseEntity.created(URI.create("/users/" + userDto.getId())).body(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid AuthRequest authRequest, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        return ResponseEntity.ok().body(service.authenticateAndGetToken(authRequest));
    }
}
