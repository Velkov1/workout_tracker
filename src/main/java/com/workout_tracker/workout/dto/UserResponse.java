package com.workout_tracker.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private List<Long> workoutIds;
    private Instant createdAt;
}
