package com.workout_tracker.workout.dto;

import com.workout_tracker.workout.model.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExerciseResponse {
    private Long id;
    private String name;
    private Level level;
}
