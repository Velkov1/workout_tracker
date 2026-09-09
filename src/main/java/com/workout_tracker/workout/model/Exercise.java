package com.workout_tracker.workout.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "exercises")
@NoArgsConstructor
@Getter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Enumerated(EnumType.STRING)
    @Setter
    private Level level;

    public Exercise(String name, Level level){
        this.name = name;
        this.level = level;
    }

    @Override
    public boolean equals(Object o){
        if(this== o){
            return true;
        }
        if(!(o instanceof Exercise)){
            return false;
        }
        Exercise exercise = (Exercise) o;
        return id != null && id.equals(exercise.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Exercise{id=" + id + ", name='" + name + "', level=" + level + '}';
    }
}
