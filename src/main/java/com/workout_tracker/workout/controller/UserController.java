package com.workout_tracker.workout.controller;

import com.workout_tracker.workout.dto.UserRequest;
import com.workout_tracker.workout.dto.UserResponse;
import com.workout_tracker.workout.dto.WorkoutResponse;
import com.workout_tracker.workout.security.CustomUserDetails;
import com.workout_tracker.workout.service.UserService;
import com.workout_tracker.workout.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final WorkoutService workoutService;

    //removing the method, because the whole function is made from the api/auth/register, giving this method not the whole functionality
    /*
    @PostMapping("")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } */

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long userId){
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/name")
    public ResponseEntity<UserResponse> changeName(@PathVariable Long userId,
                                                   @RequestParam String name){
        UserResponse response = userService.changeName(userId, name);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId ){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> all = userService.getAllUsers();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{userId}/workouts")
    public ResponseEntity<List<WorkoutResponse>> getAllWorkoutsOfUser(@PathVariable Long userId){
        List<WorkoutResponse> all = workoutService.getAllByUser(userId);
        return ResponseEntity.ok(all);
    }
}
