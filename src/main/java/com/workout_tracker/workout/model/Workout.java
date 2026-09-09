package com.workout_tracker.workout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "workouts")
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private Instant createdAt;
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;
    @Getter
    @ManyToMany
    @Setter
    @JoinTable(
            name = "workout_exercises",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private Set<Exercise> exercises;

    public Workout(String name, User createdBy, Set<Exercise> exercises){
        this.name = name;
        this.exercises = exercises;
        this.createdBy = createdBy;
        this.createdAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout)) return false;
        Workout workout = (Workout) o;
        return id != null && id.equals(workout.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Workout{id=" + id + ", name='" + name + "', createdAt=" + createdAt + '}';
    }
}
