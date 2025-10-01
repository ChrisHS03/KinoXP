package com.example.kinoxp.config;

import com.example.kinoxp.model.Genre;
import com.example.kinoxp.model.Movie;
import com.example.kinoxp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;


    @Override
    public void run(String... args) throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovie_title("Interstellar");
        movie1.setMovie_duration(169);
        movie1.setMovie_actors("Matthew McConaughey, Anne Hathaway, Jessica Chastain");
        movie1.setMovie_age_req(8);
        movie1.setMovie_period_start(LocalDate.of(2025, 10, 1));
        movie1.setMovie_period_end(LocalDate.of(2025, 12, 1));
        movie1.setMovie_genre(Genre.SCIENCE_FICTION);

        movieRepository.save(movie1);

    }
}
