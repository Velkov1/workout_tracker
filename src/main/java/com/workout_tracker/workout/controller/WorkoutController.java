package com.workout_tracker.workout.controller;

import com.workout_tracker.workout.dto.WorkoutRequest;
import com.workout_tracker.workout.dto.WorkoutResponse;
import com.workout_tracker.workout.security.CustomUserDetails;
import com.workout_tracker.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("")
    public ResponseEntity<WorkoutResponse> create(@Valid @RequestBody WorkoutRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails principal){
        WorkoutResponse response = workoutService.create(request, principal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(@PathVariable Long workoutId){
        WorkoutResponse response = workoutService.getWorkoutById(workoutId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{workoutId}/exercises/{exerciseId}/add")
    public ResponseEntity<WorkoutResponse> addExerciseToWorkout(@PathVariable Long workoutId,
                                                                @PathVariable Long exerciseId){
        WorkoutResponse response = workoutService.addExerciseToWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{workoutId}/exercises/{exerciseId}/remove")
    public ResponseEntity<WorkoutResponse> removeExerciseFromWorkout(@PathVariable Long workoutId,
                                                                @PathVariable Long exerciseId){
        WorkoutResponse response = workoutService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(response);
    }



}
