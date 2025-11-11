package com.example.demo.repository;

import com.example.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    // Zoek films op naam (case insensitive)
    List<Movie> findByNameContainingIgnoreCase(String name);
    
    // Zoek films op release jaar
    List<Movie> findByReleaseYear(Integer year);
    
    // Zoek films tussen bepaalde jaren
    List<Movie> findByReleaseYearBetween(Integer startYear, Integer endYear);
    
    // Zoek films op genre
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE LOWER(g) = LOWER(:genre)")
    List<Movie> findByGenre(@Param("genre") String genre);
    
    // Check of film met exacte naam al bestaat
    Optional<Movie> findByNameIgnoreCase(String name);
    
    // Zoek films korter dan bepaalde duur
    List<Movie> findByDurationMinutesLessThan(Integer duration);
    
    // Zoek films langer dan bepaalde duur
    List<Movie> findByDurationMinutesGreaterThan(Integer duration);
}