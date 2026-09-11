package com.workout_tracker.workout.security;

import com.workout_tracker.workout.model.Workout;
import com.workout_tracker.workout.repository.WorkoutRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("workoutSecurity")
@RequiredArgsConstructor
public class WorkoutSecurity {
    private final WorkoutRepository workoutRepository;

    public boolean isOwner(Long workoutId, Authentication authentication){
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        return workoutRepository.existsByIdAndCreatedById(workoutId, userId);
    }
}
