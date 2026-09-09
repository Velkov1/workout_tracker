package com.workout_tracker.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class WorkoutRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long userId;

    @NotEmpty
    private List<Long> exerciseIds;

}
