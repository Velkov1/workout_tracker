package com.workout_tracker.workout.service;

import com.workout_tracker.workout.dto.WorkoutRequest;
import com.workout_tracker.workout.dto.WorkoutResponse;
import com.workout_tracker.workout.exception.ExerciseNotFoundException;
import com.workout_tracker.workout.exception.UserNotFoundException;
import com.workout_tracker.workout.exception.WorkoutNotFoundException;
import com.workout_tracker.workout.model.Exercise;
import com.workout_tracker.workout.model.User;
import com.workout_tracker.workout.model.Workout;
import com.workout_tracker.workout.repository.ExerciseRepository;
import com.workout_tracker.workout.repository.UserRepository;
import com.workout_tracker.workout.repository.WorkoutRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;


    public WorkoutResponse create(WorkoutRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + request.getUserId()));
        Set<Exercise> exercises = new HashSet<>();
        for(Long id : request.getExerciseIds()){
            Exercise exercise = exerciseRepository.findById(id)
                    .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + id));
            exercises.add(exercise);
        }
        Workout workout = new Workout(
                request.getName(),
                user,
                exercises

        );
        return toWorkoutResponse(workoutRepository.save(workout));
    }

    private WorkoutResponse toWorkoutResponse(Workout workout){
        return new WorkoutResponse(
                workout.getId(),
                workout.getName(),
                workout.getCreatedBy().getId(),
                workout.getCreatedAt(),
                workout.getExercises().stream().map(Exercise :: getId).toList()
        );
    }

    public WorkoutResponse getWorkoutById(Long id){
        return toWorkoutResponse(workoutRepository.findById(id).orElseThrow(() -> new WorkoutNotFoundException("Workout not found: " + id)));
    }

    public WorkoutResponse addExerciseToWorkout(Long workoutId, Long exerciseId){
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new WorkoutNotFoundException("Workout not found: " + workoutId));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + exerciseId));
        workout.getExercises().add(exercise);
        return toWorkoutResponse(workoutRepository.save(workout));
    }

    public WorkoutResponse removeExerciseFromWorkout(Long workoutId, Long exerciseId){
        Workout workout = workoutRepository.findById(workoutId).orElseThrow(() -> new WorkoutNotFoundException("Workout not found: " + workoutId));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new ExerciseNotFoundException("Exercise not found: " + exerciseId));
        boolean isRemoved = workout.getExercises().remove(exercise);
        if(!isRemoved){
            throw new ExerciseNotFoundException("Exercise is not part of the given workout");
        }
        return toWorkoutResponse(workout);
    }

    public List<WorkoutResponse> getAllByUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return user.getWorkouts().stream().map(this::toWorkoutResponse).toList();
    }

}
