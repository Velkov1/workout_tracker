package com.workout_tracker.workout.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserRequest {
    @NotBlank
    private String name;

}
