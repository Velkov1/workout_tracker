package com.workout_tracker.workout.controller;

import com.workout_tracker.workout.dto.ExerciseRequest;
import com.workout_tracker.workout.dto.ExerciseResponse;
import com.workout_tracker.workout.model.Level;
import com.workout_tracker.workout.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping("")
    public ResponseEntity<ExerciseResponse> create(@Valid @RequestBody ExerciseRequest request){
        ExerciseResponse response = exerciseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<List<ExerciseResponse>> getAll(){
        List<ExerciseResponse> all = exerciseService.getAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long exerciseId){
        exerciseService.deleteExercise(exerciseId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{exerciseId}/name")
    public ResponseEntity<ExerciseResponse> changeName(@PathVariable Long exerciseId,
                                                       @RequestParam String name){
        ExerciseResponse response = exerciseService.changeName(exerciseId, name);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{exerciseId}/level")
    public ResponseEntity<ExerciseResponse> changeLevel(@PathVariable Long exerciseId,
                                                       @RequestParam Level level){
        ExerciseResponse response = exerciseService.changeLevel(exerciseId, level);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<ExerciseResponse>> getAllByUser(@PathVariable Long userId){
        List<ExerciseResponse> all = exerciseService.getAllByUser(userId);
        return ResponseEntity.ok(all);
    }
}
