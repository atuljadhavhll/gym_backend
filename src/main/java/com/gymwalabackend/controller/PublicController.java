package com.gymwalabackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is running successfully");
        return response;
    }

    @GetMapping("/home")
    public Map<String, Object> home() {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Welcome to Public Home Page");
        response.put("products", List.of(
                "Laptop",
                "Mobile",
                "Tablet"
        ));

        return response;
    }
}
