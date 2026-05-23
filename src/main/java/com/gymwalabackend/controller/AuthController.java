package com.gymwalabackend.controller;


import com.gymwalabackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/public")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String email,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String givenName,
                                     @RequestParam(required = false) String familyName,
                                     @RequestParam(required = false) String picture) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        if (name != null) claims.put("name", name);
        if (givenName != null) claims.put("given_name", givenName);
        if (familyName != null) claims.put("family_name", familyName);
        if (picture != null) claims.put("picture", picture);

        String token = jwtUtil.generateToken(claims);

        return Map.of("token", token);
    }


}