package com.example.demo.repository;

import com.example.demo.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.dto.RatingDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMovieId(Long movieId);

    List<Rating> findByUserId(Long userId);

    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movie.id = :movieId")
    Double getAverageRatingForMovie(@Param("movieId") Long movieId);

    Long countByMovieId(Long movieId);

    @Query ("SELECT r.movie.id, AVG(r.rating) as avgRating" +
            " FROM Rating r" +
            " GROUP BY r.movie.id" +
            " ORDER BY avgRating DESC")
    List<Object[]> findTopRatedMovies();

    @Query("SELECT new com.example.demo.dto.RatingDTO(" +
       "r.id, r.user.id, r.user.username, r.movie.id, " +
       "r.rating, r.comment, r.createdAt, r.updatedAt) " +
       "FROM Rating r WHERE r.movie.id = :movieId")
    List<RatingDTO> findRatingDTOsByMovieId(@Param("movieId") Long movieId);
}