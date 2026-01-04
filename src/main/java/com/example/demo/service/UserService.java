package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Hier komen alle business logic methods
    public User registerUser(User user) {
        // Business logic: check if username exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Business logic: encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Save user
        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    public User updateUser(Long id, User updateRequest) {
        Optional<User> userOpt = userRepository.findById(id);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User existingUser = userOpt.get();
        
        // Check if new username is taken
        if (updateRequest.getUsername() != null &&
            !updateRequest.getUsername().equals(existingUser.getUsername()) &&
            userRepository.existsByUsername(updateRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        // Update fields
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().trim().isEmpty()) {
            existingUser.setUsername(updateRequest.getUsername());
        }
        
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }
        
        return userRepository.save(existingUser);
    }
}