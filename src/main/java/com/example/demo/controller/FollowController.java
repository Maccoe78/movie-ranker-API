package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.FollowService;
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
    private FollowService followService;

    @PostMapping("/{userId}/follow/{followedUserId}")
    @Operation(summary = "Follow a user")
    public ResponseEntity<Map<String, Object>> followUser(@PathVariable Long userId, @PathVariable Long followedUserId) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = followService.followUser(userId, followedUserId);
            response.put("message", "User followed successfully");
            response.put("user", user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{userId}/follow/{followedUserId}")
    @Operation(summary = "Unfollow a user")
    public ResponseEntity<Map<String, Object>> unfollowUser(@PathVariable Long userId, @PathVariable Long followedUserId) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = followService.unfollowUser(userId, followedUserId);
            response.put("message", "User unfollowed successfully");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "Get all users that a user is following")
    public ResponseEntity<Map<String, Object>> getFollowing(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<User> following = followService.getFollowing(userId);
            response.put("following", following);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by username")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = followService.searchUsers(username);
        return ResponseEntity.ok(users);
    }

}