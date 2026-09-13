package com.workout_tracker.workout.controller;

import com.workout_tracker.workout.dto.LoginRequest;
import com.workout_tracker.workout.dto.LoginResponse;
import com.workout_tracker.workout.dto.RegisterRequest;
import com.workout_tracker.workout.dto.RegisterResponse;
import com.workout_tracker.workout.security.CustomUserDetails;
import com.workout_tracker.workout.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request){
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        LoginResponse response = authenticationService.me(userDetails.getUsername(), token);
        return ResponseEntity.ok(response);
    }

}
