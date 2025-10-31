package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User", description = "User management endpoints")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            response.put("message", "Username is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        response.put("message", "User registered successfully");
        response.put("username", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User loginRequest) {
        Map<String, String> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            response.put("message", "Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }   
        return ResponseEntity.ok(userOpt.get());
    }

    @GetMapping("/users/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve a specific user by their username")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(userOpt.get());
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User with ID " + id + " deleted successfully.");  
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/username/{username}")
    @Operation(summary = "Delete user by username") 
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username){
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok("User with username " + username + " deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update user by ID")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable Long id, @RequestBody User updateRequest) {
        Map<String, String> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.notFound().build();
        }

        User existingUser = userOpt.get();

        if (updateRequest.getUsername() != null &&
            !updateRequest.getUsername().equals(existingUser.getUsername()) &&
            userRepository.existsByUsername(updateRequest.getUsername())) {
            response.put("message", "Username is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().trim().isEmpty()) {
            existingUser.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(existingUser);

        response.put("message", "User updated successfully");
        response.put("username", existingUser.getUsername());
        return ResponseEntity.ok(response);
    }

}