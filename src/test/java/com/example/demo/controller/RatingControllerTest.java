package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.model.Rating;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
class RatingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MovieRepository movieRepository;

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
    void addRating_Success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        String requestBody = """
            {
                "userId": 1,
                "movieId": 1,
                "rating": 5,
                "comment": "Amazing movie!"
            }
            """;

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rating added successfully"))
                .andExpect(jsonPath("$.rating.id").value(1L))
                .andExpect(jsonPath("$.rating.rating").value(5))
                .andExpect(jsonPath("$.rating.comment").value("Amazing movie!"));
    }

    @Test
    void addRating_UserNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        String requestBody = """
            {
                "userId": 999,
                "movieId": 1,
                "rating": 5,
                "comment": "Amazing movie!"
            }
            """;

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void addRating_MovieNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        String requestBody = """
            {
                "userId": 1,
                "movieId": 999,
                "rating": 5,
                "comment": "Amazing movie!"
            }
            """;

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"));
    }   

    @Test
    void updateRating_Success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(ratingRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Optional.of(testRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(testRating);

        String requestBody = """
            {
                "userId": 1,
                "movieId": 1,
                "rating": 4,
                "comment": "Updated review!"
            }
            """;

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rating updated successfully"));
    }

    @Test
    void deleteRating_Success() throws Exception {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(testRating));

        mockMvc.perform(delete("/api/ratings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rating deleted successfully"));
    }
}