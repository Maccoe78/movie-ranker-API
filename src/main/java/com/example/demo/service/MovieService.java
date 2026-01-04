package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    public Movie addMovie(Movie movie) {
        // Check if movie with same name already exists
        Optional<Movie> existingMovie = movieRepository.findByNameIgnoreCase(movie.getName());
        if (existingMovie.isPresent()) {
            throw new IllegalArgumentException("A movie with this name already exists");
        }
        
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public List<Movie> searchMoviesByName(String name) {
        return movieRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Movie> getMoviesByYear(Integer year) {
        return movieRepository.findByReleaseYear(year);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    public Movie updateMovie(Long id, Movie updateRequest) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        
        if (movieOpt.isEmpty()) {
            throw new IllegalArgumentException("Movie not found");
        }
        
        Movie existingMovie = movieOpt.get();
        
        // Check if new name already exists (only if name is being changed)
        if (updateRequest.getName() != null && 
            !updateRequest.getName().equalsIgnoreCase(existingMovie.getName())) {
            Optional<Movie> duplicateMovie = movieRepository.findByNameIgnoreCase(updateRequest.getName());
            if (duplicateMovie.isPresent()) {
                throw new IllegalArgumentException("A movie with this name already exists");
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
        
        return movieRepository.save(existingMovie);
    }

    public boolean deleteMovie(Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        
        if (movieOpt.isEmpty()) {
            return false;
        }
        
        movieRepository.deleteById(id);
        return true;
    }
}