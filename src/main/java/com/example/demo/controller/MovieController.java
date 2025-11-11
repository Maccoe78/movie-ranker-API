package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Movie management endpoints")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @PostMapping
    @Operation(summary = "Add a new movie")
    public ResponseEntity<Map<String, Object>> addMovie(@Valid @RequestBody Movie movie) {
        Map<String, Object> response = new HashMap<>();

        // Check if movie with same name already exists
        Optional<Movie> existingMovie = movieRepository.findByNameIgnoreCase(movie.getName());
        if (existingMovie.isPresent()) {
            response.put("message", "A movie with this name already exists");
            response.put("existingMovie", existingMovie.get());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Movie savedMovie = movieRepository.save(movie);
        response.put("message", "Movie added successfully");
        response.put("movie", savedMovie);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        
        if (movieOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(movieOpt.get());
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies by name")
    public ResponseEntity<List<Movie>> searchMoviesByName(@RequestParam String name) {
        List<Movie> movies = movieRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get movies by release year")
    public ResponseEntity<List<Movie>> getMoviesByYear(@PathVariable Integer year) {
        List<Movie> movies = movieRepository.findByReleaseYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get movies by genre")
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieRepository.findByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie by ID")
    public ResponseEntity<Map<String, Object>> updateMovie(@PathVariable Long id, @RequestBody Movie updateRequest) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<Movie> movieOpt = movieRepository.findById(id);
        
        if (movieOpt.isEmpty()) {
            response.put("message", "Movie not found");
            return ResponseEntity.notFound().build();
        }
        
        Movie existingMovie = movieOpt.get();
        
        // Check if new name already exists (only if name is being changed)
        if (updateRequest.getName() != null && 
            !updateRequest.getName().equalsIgnoreCase(existingMovie.getName())) {
            Optional<Movie> duplicateMovie = movieRepository.findByNameIgnoreCase(updateRequest.getName());
            if (duplicateMovie.isPresent()) {
                response.put("message", "A movie with this name already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }
        
        // Update fields if provided
        if (updateRequest.getName() != null && !updateRequest.getName().trim().isEmpty()) {
            existingMovie.setName(updateRequest.getName());
        }
        if (updateRequest.getReleaseYear() != null) {
            existingMovie.setReleaseYear(updateRequest.getReleaseYear());
        }
        if (updateRequest.getDescription() != null) {
            existingMovie.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getDurationMinutes() != null) {
            existingMovie.setDurationMinutes(updateRequest.getDurationMinutes());
        }
        if (updateRequest.getGenres() != null) {
            existingMovie.setGenres(updateRequest.getGenres());
        }
        if (updateRequest.getPosterUrl() != null) {
            existingMovie.setPosterUrl(updateRequest.getPosterUrl());
        }
        
        Movie updatedMovie = movieRepository.save(existingMovie);
        
        response.put("message", "Movie updated successfully");
        response.put("movie", updatedMovie);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie by ID")
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        Optional<Movie> movieOpt = movieRepository.findById(id);
        
        if (movieOpt.isEmpty()) {
            response.put("message", "Movie not found");
            return ResponseEntity.notFound().build();
        }
        
        movieRepository.deleteById(id);
        response.put("message", "Movie with ID " + id + " deleted successfully");
        return ResponseEntity.ok(response);
    }
}