DROP SCHEMA IF EXISTS TESTDB CASCADE;

-- Opret schema igen
CREATE SCHEMA IF NOT EXISTS TESTDB;

-- Brug schema
SET SCHEMA TESTDB;

-- ===== Movie Table =====
CREATE TABLE movie
(
    movie_id           INT AUTO_INCREMENT PRIMARY KEY,
    movie_title        VARCHAR(255) NOT NULL,
    movie_description  VARCHAR(1000),
    movie_duration     INT,
    movie_actors       VARCHAR(500),
    movie_age_req      INT,
    movie_period_start DATE,
    movie_period_end   DATE,
    movie_genre        ENUM(
    'ACTION', 'ADVENTURE', 'COMEDY', 'DRAMA', 'HORROR', 'THRILLER', 'SCIENCE_FICTION', 'FANTASY',
    'ROMANCE', 'CRIME', 'MYSTERY', 'DOCUMENTARY', 'ANIMATION', 'FAMILY', 'WAR', 'WESTERN',
    'MUSICAL', 'BIOGRAPHY', 'HISTORY', 'SPORT', 'SUPERHERO'
) ,

    movie_photo_href   VARCHAR(500)
);

-- Optional: test data
INSERT INTO movie (movie_title, movie_description, movie_duration, movie_actors, movie_age_req, movie_period_start,
                   movie_period_end, movie_genre, movie_photo_href)
VALUES ('Inception', 'Mind-bending sci-fi', 148, 'Leonardo DiCaprio', 13, '2025-10-01', '2025-12-31', 'ACTION',
        'https://example.com/inception.jpg');


-- ===== Theater Table =====
CREATE TABLE theater
(
    theater_id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    seat_rows INT NOT NULL DEFAULT 0,
    seats_per_row INT NOT NULL DEFAULT 0

);

-- Optional: test data
INSERT INTO theater (name)
VALUES ('Big');
INSERT INTO theater (name)
VALUES ('Small');


-- ===== Show Table =====
CREATE TABLE shows
(
    show_id    INT AUTO_INCREMENT PRIMARY KEY,
    movie_id   INT       NOT NULL,
    theater_id INT       NOT NULL,
    show_time  TIMESTAMP NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie (movie_id),
    FOREIGN KEY (theater_id) REFERENCES theater (theater_id)
);

-- Optional: test data
INSERT INTO shows (movie_id, theater_id, show_time, price)
VALUES (1, 1, '2025-10-15 20:00:00', 120.0);
