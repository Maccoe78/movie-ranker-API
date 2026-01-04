package com.example.demo.unit;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("maccoe", "password123");
        user1.setId(1L);
        user1.setFollowing(new ArrayList<>());

        user2 = new User("jacco", "password456");
        user2.setId(2L);
    }

    @Test
    void followUser_Success() throws Exception {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        // Act
        User result = followService.followUser(1L, 2L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getFollowing().contains(user2));
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(userRepository).save(user1);
    }

    @Test
    void followUser_UserNotFound() throws Exception {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followService.followUser(999L, 2L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).findById(2L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void unfollowUser_Success() {
        // Arrange
        user1.getFollowing().add(user2);  // user1 volgt al user2
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        // Act
        User result = followService.unfollowUser(1L, 2L);

        // Assert
        assertNotNull(result);
        assertFalse(result.getFollowing().contains(user2));
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(userRepository).save(user1);
    }

    @Test
    void getFollowing_Success() {
        // Arrange
        user1.getFollowing().add(user2);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        // Act
        List<User> result = followService.getFollowing(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(user2));
        verify(userRepository).findById(1L);
    }

    @Test
    void searchUsers_Success() {
        // Arrange
        List<User> users = List.of(user1, user2);
        when(userRepository.findByUsernameContainingIgnoreCase("acc")).thenReturn(users);

        // Act
        List<User> result = followService.searchUsers("acc");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findByUsernameContainingIgnoreCase("acc");
    }
}