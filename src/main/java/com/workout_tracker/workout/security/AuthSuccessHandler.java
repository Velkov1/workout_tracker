package com.workout_tracker.workout.security;

import com.workout_tracker.workout.dto.LoginResponse;
import com.workout_tracker.workout.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;
    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String username = user.getAttribute("email");
        String name = user.getAttribute("name");

        LoginResponse loginResponse = authenticationService.oAuth2LogIn(username, name);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", loginResponse.getToken())
                .build()
                .toUriString();

        response.sendRedirect(targetUrl);

    }
}
