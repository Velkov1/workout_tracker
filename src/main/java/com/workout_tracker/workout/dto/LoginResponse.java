package com.workout_tracker.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String token;
    private String username;
    private UserResponse userResponse;
}
