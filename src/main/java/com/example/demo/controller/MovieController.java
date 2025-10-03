package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie Controller", description = "Operations for managing movies in the ranking system")
public class MovieController {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @GetMapping
    @Operation(summary = "Get all movies", description = "Retrieve a list of all movies in the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of movies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID", description = "Retrieve a specific movie by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie found"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "ID of the movie to retrieve", example = "1")
            @PathVariable Long id) {
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new movie", description = "Add a new movie to the ranking system")
    @ApiResponse(responseCode = "200", description = "Movie created successfully")
    public Movie createMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a movie", description = "Update an existing movie by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "ID of the movie to update", example = "1")
            @PathVariable Long id, 
            @RequestBody Movie movieDetails) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            Movie existingMovie = movie.get();
            existingMovie.setTitle(movieDetails.getTitle());
            existingMovie.setDirector(movieDetails.getDirector());
            existingMovie.setYear(movieDetails.getYear());
            existingMovie.setGenre(movieDetails.getGenre());
            existingMovie.setRating(movieDetails.getRating());
            return ResponseEntity.ok(movieRepository.save(existingMovie));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie", description = "Remove a movie from the ranking system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movie deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public ResponseEntity<?> deleteMovie(
            @Parameter(description = "ID of the movie to delete", example = "1")
            @PathVariable Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get movies by genre", description = "Retrieve all movies of a specific genre")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movies by genre")
    public List<Movie> getMoviesByGenre(
            @Parameter(description = "Genre of movies to retrieve", example = "Drama")
            @PathVariable String genre) {
        return movieRepository.findByGenre(genre);
    }
    
    @GetMapping("/year/{year}")
    @Operation(summary = "Get movies by year", description = "Retrieve all movies from a specific year")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movies by year")
    public List<Movie> getMoviesByYear(
            @Parameter(description = "Release year of movies to retrieve", example = "1994")
            @PathVariable Integer year) {
        return movieRepository.findByYear(year);
    }
    
    @GetMapping("/rating/{minRating}")
    @Operation(summary = "Get movies by minimum rating", description = "Retrieve all movies with rating greater than or equal to the specified value")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved movies by rating")
    public List<Movie> getMoviesByRating(
            @Parameter(description = "Minimum rating (1-10)", example = "8.0")
            @PathVariable Double minRating) {
        return movieRepository.findByRatingGreaterThanEqual(minRating);
    }
}