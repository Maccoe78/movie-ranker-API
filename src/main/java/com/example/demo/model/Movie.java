package com.example.demo.model;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "movies")
@Schema(description = "Movie entity representing a movie in the ranker system")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the movie", example = "1")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Title of the movie", example = "The Shawshank Redemption")
    private String title;
    
    @Column
    @Schema(description = "Director of the movie", example = "Frank Darabont")
    private String director;
    
    @Column
    @Schema(description = "Release year of the movie", example = "1994")
    private Integer year;
    
    @Column
    @Schema(description = "Genre of the movie", example = "Drama")
    private String genre;
    
    @Column
    @Schema(description = "Rating of the movie (1-10)", example = "9.3", minimum = "1", maximum = "10")
    private Double rating;
    
    // Constructors
    public Movie() {}
    
    public Movie(String title, String director, Integer year, String genre, Double rating) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
}