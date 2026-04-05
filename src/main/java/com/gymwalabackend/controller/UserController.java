package com.gymwalabackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {


    @GetMapping("/user")
    public Map<String, Object> user(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return Map.of(
                    "email", jwt.getSubject(),
                    "token", jwt.getTokenValue()
            );
        } else if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauthUser) {
            return Map.of(
                    "email", oauthUser.getAttribute("email"),
                    "name", oauthUser.getAttribute("name"),
                    "photo", oauthUser.getAttribute("picture")
            );
        } else {
            return Map.of("error", "Unsupported principal type: " + principal.getClass().getName());
        }
    }
}
