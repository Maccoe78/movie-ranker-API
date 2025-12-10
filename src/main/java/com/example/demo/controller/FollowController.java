package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/follows")
@Tag(name = "Follows", description = "Follow management endpoints")
@CrossOrigin(origins = "*")
public class FollowController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}/follow/{followedUserId}")
    @Operation(summary = "Follow a user")
    public ResponseEntity<Map<String, Object>> followUser(@PathVariable Long userId, @PathVariable Long followedUserId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<User> followedUserOpt = userRepository.findById(followedUserId);
        if (followedUserOpt.isEmpty()) {
            response.put("message", "User to follow not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        User followedUser = followedUserOpt.get();

        // Add followed user to user's following list
        user.getFollowing().add(followedUser);
        userRepository.save(user);

        response.put("message", "User followed successfully");
        response.put("user", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{userId}/follow/{followedUserId}")
    @Operation(summary = "Unfollow a user")
    public ResponseEntity<Map<String, Object>> unfollowUser(@PathVariable Long userId, @PathVariable Long followedUserId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<User> followedUserOpt = userRepository.findById(followedUserId);
        if (followedUserOpt.isEmpty()) {
            response.put("message", "User to unfollow not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        User followedUser = followedUserOpt.get();

        // Remove followed user from user's following list
        user.getFollowing().remove(followedUser);
        userRepository.save(user);

        response.put("message", "User unfollowed successfully");
        response.put("user", user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "Get all users that a user is following")
    public ResponseEntity<Map<String, Object>> getFollowing(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        List<User> following = user.getFollowing();

        response.put("following", following);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by username")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        return ResponseEntity.ok(users);
    }

}