package com.workout_tracker.workout.exception;

public class UsernameAlreadyExistingException extends RuntimeException {
    public UsernameAlreadyExistingException(String message) {
        super(message);
    }
}
