package com.workout_tracker.workout.security;

import com.workout_tracker.workout.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class LoginLimitFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration WINDOW = Duration.ofMinutes(5);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isLoginRequest = request.getRequestURI().equals("/api/auth/login")
                && request.getMethod().equals("POST");
        if(!isLoginRequest){
            filterChain.doFilter(request,response);
            return;
        }
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        LoginRequest loginRequest = objectMapper.readValue(wrappedRequest.getInputStream(), LoginRequest.class);
        String username = loginRequest.getUsername();
        String key = "login-fail:username:" + username;


        String currentCount = redisTemplate.opsForValue().get(key);
        if(currentCount != null && Integer.parseInt(currentCount) >= MAX_ATTEMPTS){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many failed login attempts. Try again later.");
            return;
        }
        filterChain.doFilter(wrappedRequest, response);

        if(response.getStatus() == 404){
            Long attempts = redisTemplate.opsForValue().increment(key);
            if(attempts != null && attempts == 1){
                redisTemplate.expire(key, WINDOW);
            }
        }
        else if(response.getStatus() == 200){
            redisTemplate.delete(key);
        }
    }
}
