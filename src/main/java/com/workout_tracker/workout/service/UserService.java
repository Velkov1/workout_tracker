package com.workout_tracker.workout.service;

import com.workout_tracker.workout.dto.UserRequest;
import com.workout_tracker.workout.dto.UserResponse;
import com.workout_tracker.workout.exception.UserNotFoundException;
import com.workout_tracker.workout.model.User;
import com.workout_tracker.workout.model.Workout;
import com.workout_tracker.workout.repository.ExerciseRepository;
import com.workout_tracker.workout.repository.UserRepository;
import com.workout_tracker.workout.repository.WorkoutRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    public UserResponse create(UserRequest request){
        User user = new User(
                request.getName(),
                new ArrayList<>()
        );
        return toUserResponse(userRepository.save(user));
    }

    private UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getWorkouts().stream().map(Workout::getId).toList(),
                user.getCreatedAt()
        );
    }

    public UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        return toUserResponse(user);
    }

    public UserResponse changeName(Long id, String name){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        user.setName(name);
        return toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        userRepository.delete(user);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }



}
