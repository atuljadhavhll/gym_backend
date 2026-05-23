package com.gymwalabackend.repository;

import com.gymwalabackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    // Find user by email
    Optional<Users> findByEmail(String email);

    // Find user by username
    Optional<Users> findByUsername(String username);

    // Find user by userId
    Optional<Users> findByUserId(Integer userId);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);
}
