package com.example.demo.integration;

import com.example.demo.model.Movie;
import com.example.demo.model.User;
import com.example.demo.model.Rating;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class RatingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    private User testUser;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        movieRepository.deleteAll();

        testUser = new User("integrationuser", "password123");
        testUser = userRepository.save(testUser);

        testMovie = new Movie();
        testMovie.setName("Integration Test Movie");
        testMovie.setReleaseYear(2024);
        testMovie.setDescription("Test movie for integrtion");
        testMovie.setDurationMinutes(120);
        testMovie = movieRepository.save(testMovie);
    }

    @Test
    void addRating_FullWorkflow() throws Exception {
        String addRequest = String.format("""
            {
                "userId": %d,
                "movieId": %d,
                "rating": 5,
                "comment": "Integration test rating!"
            }
            """, testUser.getId(), testMovie.getId());

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rating saved successfully"))
                .andExpect(jsonPath("$.rating.rating").value(5))
                .andExpect(jsonPath("$.rating.comment").value("Integration test rating!"));

        mockMvc.perform(get("/api/ratings/movie/" + testMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRatings").value(1))
                .andExpect(jsonPath("$.averageRating").value(5.0))
                .andExpect(jsonPath("$.ratings[0].userName").value("integrationuser"))
                .andExpect(jsonPath("$.ratings[0].comment").value("Integration test rating!"));
    }

    @Test
    void updateRating_Workflow() throws Exception {
        String addRequest = String.format("""
            {
                "userId": %d,
                "movieId": %d,
                "rating": 3,
                "comment": "Original rating"
            }
            """, testUser.getId(), testMovie.getId());

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addRequest))
                .andExpect(status().isOk());

        String updateRequest = String.format("""
            {
                "userId": %d,
                "movieId": %d,
                "rating": 5,
                "comment": "Updated rating!"
            }
            """, testUser.getId(), testMovie.getId());

        mockMvc.perform(post("/api/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rating saved successfully"));

        mockMvc.perform(get("/api/ratings/movie/" + testMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRatings").value(1)) 
                .andExpect(jsonPath("$.averageRating").value(5.0))  
                .andExpect(jsonPath("$.ratings[0].comment").value("Updated rating!"));
    }
}