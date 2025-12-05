package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.model.Rating;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.RatingDTO;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Ratings", description = "Rating management endpoints")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping
    @Operation(summary = "Add or update a rating")
    public ResponseEntity<Map<String, Object>> addOrUpdateRating(@Valid @RequestBody RatingRequest request) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<Movie> movieOpt = movieRepository.findById(request.getMovieId());
        if (movieOpt.isEmpty()) {
            response.put("message", "Movie not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();
        Movie movie = movieOpt.get();

        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndMovieId(
            request.getUserId(),
            request.getMovieId()
        );

        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.setRating(request.getRating());
            rating.setComment(request.getComment());
            response.put("message", "Rating updated successfully");
        } else {
            rating = new Rating(user, movie, request.getRating(), request.getComment());
            response.put("message", "Rating added successfully");
        }

        Rating savedRating = ratingRepository.save(rating);
        response.put("rating", savedRating);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Get all ratings for a movie")
    public ResponseEntity<Map<String, Object>> getRatingsByMovie(@PathVariable Long movieId) {
        Map<String, Object> response = new HashMap<>();

        List<RatingDTO> ratingDTOs = ratingRepository.findRatingDTOsByMovieId(movieId);
        Double averageRating = ratingRepository.getAverageRatingForMovie(movieId);
        Long totalRatings = ratingRepository.countByMovieId(movieId);

        response.put("ratings", ratingDTOs);
        response.put("averageRating", averageRating);
        response.put("totalRatings", totalRatings);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all ratings by a user")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable Long userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/user/{userId}/movie/{movieId}")
    @Operation(summary = "Get rating by user for specific movie")
    public ResponseEntity<Rating> getRatingByUserAndMovie(
            @PathVariable Long userId,
            @PathVariable Long movieId) {
            
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if (ratingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
            
        }
        return ResponseEntity.ok(ratingOpt.get());
        
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a rating")
    public ResponseEntity<Map<String, String>> deleteRating(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        Optional<Rating> ratingOpt = ratingRepository.findById(id);
        if (ratingOpt.isEmpty()) {
            response.put("message", "Rating not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ratingRepository.deleteById(id);
        response.put("message", "Rating deleted successfully");
        return ResponseEntity.ok(response);
    }

    public static class RatingRequest {
        @NotNull
        private Long userId;

        @NotNull
        private Long movieId;

        @NotNull
        @Min(1)
        @Max(5)
        private Integer rating;

        @Size(max = 1000)
        private String comment;

        public Long getUserId() {return userId;}
        public void setUserId(Long userId) {this.userId = userId;}

        public Long getMovieId() {return movieId;}
        public void setMovieId(Long movieId) {this.movieId = movieId;}

        public Integer getRating() {return rating;}
        public void setRating(Integer rating) {this.rating = rating;}
        
        public String getComment() {return comment;}
        public void setComment(String comment) {this.comment = comment;}
    }
}