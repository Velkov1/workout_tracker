package com.workout_tracker.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class RegisterResponse {
    private Long id;
    private String username;
}
