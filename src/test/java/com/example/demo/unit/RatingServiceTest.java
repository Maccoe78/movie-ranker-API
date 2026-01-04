package com.example.demo.unit;

import com.example.demo.dto.RatingDTO;
import com.example.demo.model.Movie;
import com.example.demo.model.Rating;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {
    
    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RatingService ratingService;

    private User testUser;
    private Movie testMovie;
    private Rating testRating;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "password123");
        testUser.setId(1L);

        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setName("The Matrix");
        testMovie.setReleaseYear(1999);
        testMovie.setDescription("A sci-fi classic");
        testMovie.setDurationMinutes(136);

        testRating = new Rating(testUser, testMovie, 5, "Amazing movie!");
        testRating.setId(1L);
    }

    @Test
    void addOrUpdateRating_NewRating_Success() throws Exception {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        // Act
        Rating result = ratingService.addOrUpdateRating(1L, 1L, 5, "Amazing movie!");

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Amazing movie!", result.getComment());
        verify(userRepository).findById(1L);
        verify(movieRepository).findById(1L);
        verify(ratingRepository).findByUserIdAndMovieId(1L, 1L);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void addOrUpdateRating_UpdateExistingRating_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(testRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        // Act
        Rating result = ratingService.addOrUpdateRating(1L, 1L, 4, "Updated review!");

        // Assert
        assertNotNull(result);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    void addOrUpdateRating_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addOrUpdateRating(999L, 1L, 5, "Great!");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(999L);
        verify(movieRepository, never()).findById(any());
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void addOrUpdateRating_MovieNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addOrUpdateRating(1L, 999L, 5, "Great!");
        });

        assertEquals("Movie not found", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(movieRepository).findById(999L);
        verify(ratingRepository, never()).save(any());
    }  

    @Test
    void getRatingByUserAndMovie_Success() {
        // Arrange
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(testRating));

        // Act
        Optional<Rating> result = ratingService.getRatingByUserAndMovie(1L, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testRating, result.get());
        verify(ratingRepository).findByUserIdAndMovieId(1L, 1L);
    }

    @Test
    void deleteRating_Success() {
        // Arrange
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(testRating));

        // Act
        boolean result = ratingService.deleteRating(1L);

        // Assert
        assertTrue(result);
        verify(ratingRepository).findById(1L);
        verify(ratingRepository).deleteById(1L);
    }
}