package com.gymwalabackend.services;
import com.gymwalabackend.entity.Users;
import com.gymwalabackend.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final UsersRepository UsersRepository;

    public UsersService(UsersRepository UsersRepository) {
        this.UsersRepository = UsersRepository;
    }

    // Register new user
    public Optional<Users> registerUser(Users Users) {
        try {
            if (UsersRepository.existsByEmail(Users.getEmail())) {
                logger.warn("Attempt to register with existing email: {}", Users.getEmail());
                return Optional.empty();
            }
            if (UsersRepository.existsByUsername(Users.getUsername())) {
                logger.warn("Attempt to register with existing username: {}", Users.getUsername());
                return Optional.empty();
            }

            Users savedUser = UsersRepository.save(Users);
            logger.info("User registered successfully with ID: {}", savedUser.getUserId());
            return Optional.of(savedUser);

        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    // Find user by email
    public Optional<Users> findByEmail(String email) {
        try {
            Optional<Users> user = UsersRepository.findByEmail(email);
            if (user.isPresent()) {
                logger.info("User found by email: {}", email);
            } else {
                logger.warn("No user found with email: {}", email);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by email {}: {}", email, e.getMessage(), e);
            return Optional.empty();
        }
    }

    // Find user by ID
    public Optional<Users> findByUserId(Integer userId) {
        try {
            Optional<Users> user = UsersRepository.findByUserId(userId);
            if (user.isPresent()) {
                logger.info("User found by ID: {}", userId);
            } else {
                logger.warn("No user found with ID: {}", userId);
            }
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by ID {}: {}", userId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    // Update last login timestamp
    public boolean updateLastLogin(Integer userId) {
        try {
            Optional<Users> userOpt = UsersRepository.findByUserId(userId);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                user.setLastLogin(new java.sql.Timestamp(System.currentTimeMillis()));
                UsersRepository.save(user);
                logger.info("Last login updated for user ID: {}", userId);
                return true;
            } else {
                logger.warn("Cannot update last login, user not found with ID: {}", userId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error updating last login for user ID {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    // Deactivate user
    public boolean deactivateUser(Integer userId) {
        try {
            Optional<Users> userOpt = UsersRepository.findByUserId(userId);
            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                user.setStatus(Users.Status.INACTIVE);
                UsersRepository.save(user);
                logger.info("User deactivated with ID: {}", userId);
                return true;
            } else {
                logger.warn("Cannot deactivate, user not found with ID: {}", userId);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error deactivating user ID {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }
}
