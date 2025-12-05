package com.example.demo.dto;

import java.time.LocalDateTime;

public class RatingDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long movieId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RatingDTO(Long id, Long userId, String userName, Long movieId, Integer rating, String comment, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.movieId= movieId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdDate;
        this.updatedAt = updatedDate;
    }

        public Long getId() {return id;}
        public void setId(Long id) {this.id = id;}
        
        public Long getUserId() {return userId;}
        public void setUserId(Long userId) {this.userId = userId;}

        public String getUserName() {return userName;}
        public void setUserName(String userName) {this.userName = userName;}

        public Long getMovieId() {return movieId;}
        public void setMovieId(Long movieId) {this.movieId = movieId;}

        public Integer getRating() {return rating;}
        public void setRating(Integer rating) {this.rating = rating;}
        
        public String getComment() {return comment;}
        public void setComment(String comment) {this.comment = comment;}

        public LocalDateTime getCreatedAt() {return createdAt;}
        public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

        public LocalDateTime getUpdatedAt() {return updatedAt;}
        public void setUpdatedAt(LocalDateTime updatedAt) {this.updatedAt = updatedAt;}
}