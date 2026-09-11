package com.workout_tracker.workout.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    public boolean hasAccess(Long userId, Authentication authentication){
        Long requestId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        return userId.equals(requestId);
    }
}
