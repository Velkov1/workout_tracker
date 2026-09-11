package com.workout_tracker.workout.service;

import com.workout_tracker.workout.dto.ExerciseRequest;
import com.workout_tracker.workout.dto.ExerciseResponse;
import com.workout_tracker.workout.exception.ExerciseNotFoundException;
import com.workout_tracker.workout.exception.UserNotFoundException;
import com.workout_tracker.workout.model.Exercise;
import com.workout_tracker.workout.model.Level;
import com.workout_tracker.workout.model.User;
import com.workout_tracker.workout.repository.ExerciseRepository;
import com.workout_tracker.workout.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ExerciseService {
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    public ExerciseResponse create(ExerciseRequest request){
        return  toExerciseResponse(exerciseRepository.save(new Exercise(request.getName(), request.getLevel())));
    }

    private ExerciseResponse toExerciseResponse(Exercise exercise){
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getLevel()
        );
    }
    public void deleteExercise(Long id){
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + id));
        exerciseRepository.delete(exercise);
    }

    public ExerciseResponse changeName(Long id, String name){
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + id));
        exercise.setName(name);
        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    public ExerciseResponse changeLevel(Long id, Level level){
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + id));
        exercise.setLevel(level);
        return toExerciseResponse(exerciseRepository.save(exercise));
    }

    @PreAuthorize("@userSecurity.hasAccess(#userId, authentication)")
    public List<ExerciseResponse> getAllByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return user.getWorkouts().stream()
                .flatMap(workout -> workout.getExercises().stream())
                .distinct()
                .map(this::toExerciseResponse)
                .toList();
    }
}
