package com.workout_tracker.workout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class WorkoutRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<Long> exerciseIds;

}
