
package com.repairloop.service;

import com.repairloop.entity.User;
import com.repairloop.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new customer
    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        // Never allow public registration to assign ADMIN.
        user.setRole("CUSTOMER");

        // Store the hashed password, not the original password.
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    // Find a user by email
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Verify a submitted password
    public boolean verifyPassword(
            String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(
                rawPassword, encodedPassword
        );
    }
}
