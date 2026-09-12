package com.workout_tracker.workout.service;

import com.workout_tracker.workout.dto.*;
import com.workout_tracker.workout.exception.UsernameAlreadyExistingException;
import com.workout_tracker.workout.exception.UsernameNotFoundException;
import com.workout_tracker.workout.exception.WrongPasswordException;
import com.workout_tracker.workout.jwt.JWTUtil;
import com.workout_tracker.workout.model.User;
import com.workout_tracker.workout.model.Workout;
import com.workout_tracker.workout.repository.UserCredentialsRepository;
import com.workout_tracker.workout.repository.UserRepository;
import com.workout_tracker.workout.security.Role;
import com.workout_tracker.workout.security.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public RegisterResponse register(RegisterRequest request){
        String username = request.getUsername();
        if(userCredentialsRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistingException("Username already in use: " + username);
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getName(), new ArrayList<>());
        userRepository.save(user);
        UserCredentials userCredentials = new UserCredentials(
                username,
                hashedPassword,
                Role.USER,
                user
        );
        userCredentialsRepository.save(userCredentials);
        return toRegisterResponse(userCredentials);
    }

    private RegisterResponse toRegisterResponse(UserCredentials credentials){
        return new RegisterResponse(
                credentials.getId(),
                credentials.getUsername()
        );
    }

    public LoginResponse login(LoginRequest request){
        UserCredentials credentials = userCredentialsRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + request.getUsername()));
        if(!passwordEncoder.matches(request.getPassword(), credentials.getPassword())){
            throw new WrongPasswordException("Wrong password.Try again.");
        }
        User user = credentials.getUser();
        String token = jwtUtil.generateToken(credentials.getUsername(), credentials.getRole().name());
        return new LoginResponse(
                token,
                request.getUsername(),
                new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getWorkouts().stream().map(Workout::getId).toList(),
                        user.getCreatedAt()
                )
        );
    }
}
