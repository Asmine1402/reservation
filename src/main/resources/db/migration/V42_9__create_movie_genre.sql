CREATE TABLE movie_genres
(
    movie_id UUID  NOT NULL,
    genre    genre NOT NULL,
    CONSTRAINT fk_movie_genres_movie FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
    CONSTRAINT pk_movie_genres PRIMARY KEY (movie_id, genre)
);