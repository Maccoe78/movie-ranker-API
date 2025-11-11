-- Initial schema for movie ranker application
-- Creates movies table and movie_genres table

DROP TABLE IF EXISTS movie_genres CASCADE;
DROP TABLE IF EXISTS movies CASCADE;

CREATE TABLE movies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    release_year INTEGER NOT NULL,
    description TEXT,
    duration_minutes INTEGER,
    poster_url VARCHAR(1000)
);

CREATE TABLE movie_genres (
    movie_id BIGINT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Add indexes for better performance
CREATE INDEX idx_movies_name ON movies(name);
CREATE INDEX idx_movies_release_year ON movies(release_year);
CREATE INDEX idx_movie_genres_movie_id ON movie_genres(movie_id);
CREATE INDEX idx_movie_genres_genre ON movie_genres(genre);