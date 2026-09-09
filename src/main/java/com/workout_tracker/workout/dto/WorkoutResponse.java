package com.workout_tracker.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
public class WorkoutResponse {

    private Long id;
    private String name;
    private Long userId;
    private Instant createdAt;
    private List<Long> exerciseIds;
}
