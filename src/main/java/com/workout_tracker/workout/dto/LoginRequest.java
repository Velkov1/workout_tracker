package com.workout_tracker.workout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
