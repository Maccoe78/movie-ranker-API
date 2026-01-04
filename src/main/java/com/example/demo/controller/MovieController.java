package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
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
    private MovieService movieService;

    @PostMapping
    @Operation(summary = "Add a new movie")
    public ResponseEntity<Map<String, Object>> addMovie(@Valid @RequestBody Movie movie) {
        Map<String, Object> response = new HashMap<>();

        try {
            Movie savedMovie = movieService.addMovie(movie);
            response.put("message", "Movie added successfully");
            response.put("movie", savedMovie);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOpt = movieService.getMovieById(id);
        return movieOpt.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search movies by name")
    public ResponseEntity<List<Movie>> searchMoviesByName(@RequestParam String name) {
        List<Movie> movies = movieService.searchMoviesByName(name);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Get movies by release year")
    public ResponseEntity<List<Movie>> getMoviesByYear(@PathVariable Integer year) {
        List<Movie> movies = movieService.getMoviesByYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get movies by genre")
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie by ID")
    public ResponseEntity<Map<String, Object>> updateMovie(@PathVariable Long id, @RequestBody Movie updateRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Movie updatedMovie = movieService.updateMovie(id, updateRequest);
            response.put("message", "Movie updated successfully");
            response.put("movie", updatedMovie);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            
            // Bepaal de juiste status code op basis van de error message
            if (e.getMessage().equals("Movie not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie by ID")
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        boolean deleted = movieService.deleteMovie(id);
    
        if (!deleted) {
            response.put("message", "Movie not found");
            return ResponseEntity.notFound().build();
        }
        
        response.put("message", "Movie with ID " + id + " deleted successfully");
        return ResponseEntity.ok(response);
    }
}