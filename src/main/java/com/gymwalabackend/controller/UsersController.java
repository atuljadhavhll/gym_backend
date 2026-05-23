package com.gymwalabackend.controller;
import com.gymwalabackend.entity.Users;
import com.gymwalabackend.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService UsersService;

    public UsersController(UsersService UsersService) {
        this.UsersService = UsersService;
    }

    // --- Register User ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users Users) {
        try {
            Optional<Users> registeredUser = UsersService.registerUser(Users);
            if (registeredUser.isPresent()) {
                logger.info("User registered successfully: {}", registeredUser.get().getEmail());
                return ResponseEntity.ok(registeredUser.get());
            } else {
                logger.warn("Registration failed for email: {}", Users.getEmail());
                return ResponseEntity.badRequest().body("Registration failed: Email/Username already exists.");
            }
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Unexpected error occurred during registration.");
        }
    }

    // --- Find User by Email ---
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<Users> user = UsersService.findByEmail(email);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("User not found with email: {}", email);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error fetching user by email {}: {}", email, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error fetching user details.");
        }
    }

    // --- Find User by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            Optional<Users> user = UsersService.findByUserId(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        logger.warn("User not found with ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error fetching user by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error fetching user details.");
        }
    }

    // --- Update Last Login (Token-based operation placeholder) ---
    @PutMapping("/{id}/last-login")
    public ResponseEntity<?> updateLastLogin(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        try {
            // 🔑 Token validation placeholder (JWT/OAuth2)
            if (token == null || token.isBlank()) {
                logger.warn("Unauthorized access attempt for user ID: {}", id);
                return ResponseEntity.status(401).body("Unauthorized: Missing or invalid token.");
            }

            boolean updated = UsersService.updateLastLogin(id);
            if (updated) {
                return ResponseEntity.ok("Last login updated successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error updating last login for user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error updating last login.");
        }
    }

    // --- Deactivate User (Token-based operation placeholder) ---
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        try {
            // 🔑 Token validation placeholder
            if (token == null || token.isBlank()) {
                logger.warn("Unauthorized deactivation attempt for user ID: {}", id);
                return ResponseEntity.status(401).body("Unauthorized: Missing or invalid token.");
            }

            boolean deactivated = UsersService.deactivateUser(id);
            if (deactivated) {
                return ResponseEntity.ok("User deactivated successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deactivating user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error deactivating user.");
        }
    }
}
