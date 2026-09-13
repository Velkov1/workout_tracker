package com.workout_tracker.workout.repository;

import com.workout_tracker.workout.model.User;
import com.workout_tracker.workout.security.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByUsername(String username);

    String user(User user);
}
