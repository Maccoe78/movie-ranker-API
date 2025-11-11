-- Insert sample movie data with poster URLs

INSERT INTO movies (name, release_year, description, duration_minutes, poster_url) VALUES 
(
    'The Shawshank Redemption', 
    1994, 
    'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 
    142, 
    'https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg'
),
(
    'The Dark Knight', 
    2008, 
    'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', 
    152, 
    'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg'
),
(
    'Pulp Fiction', 
    1994, 
    'The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption.', 
    154, 
    'https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg'
),
(
    'Forrest Gump', 
    1994, 
    'The presidencies of Kennedy and Johnson, the Vietnam War, and other historical events unfold from the perspective of an Alabama man with an IQ of 75.', 
    142, 
    'https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg'
),
(
    'Inception', 
    2010, 
    'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', 
    148, 
    'https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg'
);

-- Insert genres for the movies
INSERT INTO movie_genres (movie_id, genre) VALUES 
-- The Shawshank Redemption
(1, 'Drama'),
-- The Dark Knight  
(2, 'Action'),
(2, 'Crime'),
(2, 'Drama'),
-- Pulp Fiction
(3, 'Crime'),
(3, 'Drama'),
-- Forrest Gump
(4, 'Drama'),
(4, 'Romance'),
-- Inception
(5, 'Action'),
(5, 'Sci-Fi'),
(5, 'Thriller');