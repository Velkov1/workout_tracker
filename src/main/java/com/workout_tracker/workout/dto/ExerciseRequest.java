package com.workout_tracker.workout.dto;

import com.workout_tracker.workout.model.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExerciseRequest {
    @NotBlank
    private String name;

    @NotNull
    private Level level;

}
