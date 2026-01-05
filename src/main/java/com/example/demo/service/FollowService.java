package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User followUser(Long userId, Long followedUserId) {
        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Check if user to follow exists
        Optional<User> followedUserOpt = userRepository.findById(followedUserId);
        if (followedUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User to follow not found");
        }
        
        User user = userOpt.get();
        User followedUser = followedUserOpt.get();
        
        // Add to following list
        user.getFollowing().add(followedUser);
        
        return userRepository.save(user);
    }

    public User unfollowUser(Long userId, Long followedUserId) {
        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Check if user to unfollow exists
        Optional<User> followedUserOpt = userRepository.findById(followedUserId);
        if (followedUserOpt.isEmpty()) {
            throw new IllegalArgumentException("User to unfollow not found");
        }
        
        User user = userOpt.get();
        User followedUser = followedUserOpt.get();
        
        // Remove from following list
        user.getFollowing().remove(followedUser);
        
        return userRepository.save(user);
    }

    public List<User> getFollowing(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        return user.getFollowing();
    }

    public List<User> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
}