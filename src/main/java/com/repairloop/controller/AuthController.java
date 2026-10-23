
package com.repairloop.controller;

import com.repairloop.dto.RegisterRequest;
import com.repairloop.entity.User;
import com.repairloop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterRequest request) {

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());

        try {
            User savedUser = userService.registerUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                    "message", "Registration successful",
                    "userId", savedUser.getId(),
                    "fullName", savedUser.getFullName(),
                    "email", savedUser.getEmail()
                )
            );

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error", exception.getMessage())
            );
        }
    }
}
