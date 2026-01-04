package com.example.demo.service;

import com.example.demo.dto.RatingDTO;
import com.example.demo.model.Movie;
import com.example.demo.model.Rating;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    // Business logic methods komen hier
    public Rating addOrUpdateRating(Long userId, Long movieId, Integer ratingValue, String comment) {
        // Check if user exists
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Check if movie exists
        Optional<Movie> movieOpt = movieRepository.findById(movieId);
        if (movieOpt.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        
        User user = userOpt.get();
        Movie movie = movieOpt.get();
        
        // Check if rating already exists
        Optional<Rating> existingRatingOpt = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        
        Rating rating;
        if (existingRatingOpt.isPresent()) {
            // Update existing rating
            rating = existingRatingOpt.get();
            rating.setRating(ratingValue);
            rating.setComment(comment);
        } else {
            // Create new rating
            rating = new Rating(user, movie, ratingValue, comment);
        }
        
        return ratingRepository.save(rating);
    }

    public List<RatingDTO> getRatingsByMovie(Long movieId) {
        return ratingRepository.findRatingDTOsByMovieId(movieId);
    }

    public Double getAverageRatingForMovie(Long movieId) {
        return ratingRepository.getAverageRatingForMovie(movieId);
    }

    public Long getTotalRatingsForMovie(Long movieId) {
        return ratingRepository.countByMovieId(movieId);
    }

    public List<Rating> getRatingsByUser(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    public Optional<Rating> getRatingByUserAndMovie(Long userId, Long movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId);
    }   

    public boolean deleteRating(Long id) {
        Optional<Rating> ratingOpt = ratingRepository.findById(id);
        
        if (ratingOpt.isEmpty()) {
            return false;
        }
        
        ratingRepository.deleteById(id);
        return true;
    }
}