package com.workout_tracker.workout.security;

import com.workout_tracker.workout.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credentials")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserCredentials(String username, String password, Role role, AuthType authType, User user) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.authType = authType;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCredentials)) {
            return false;
        }
        UserCredentials credentials = (UserCredentials) o;
        return id != null && id.equals(credentials.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "UserCredentials{id=" + id + ", username='" + username + "', role=" + role + '}';
    }

}
