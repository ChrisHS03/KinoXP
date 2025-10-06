package com.example.kinoxp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.kinoxp.repository.*;
import com.example.kinoxp.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowRepository showRepository;


    @Override
    public void run(String... args) throws Exception {
        Movie movie1 = new Movie();
        movie1.setMovie_title("Interstellar");
        movie1.setMovie_description("Yay we are in space");
        movie1.setMovie_duration(169);
        movie1.setMovie_actors("Matthew McConaughey, Anne Hathaway, Jessica Chastain");
        movie1.setMovie_age_req(8);
        movie1.setMovie_period_start(LocalDate.of(2025, 10, 1));
        movie1.setMovie_period_end(LocalDate.of(2025, 12, 1));
        movie1.setMovie_genre(Genre.SCIENCE_FICTION);
        movie1.setMovie_photo_href("https://m.media-amazon.com/images/M/MV5BYzdjMDAxZGItMjI2My00ODA1LTlkNzItOWFjMDU5ZDJlYWY3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg");

        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setMovie_title("Interstellar2");
        movie2.setMovie_description("Yay we are in space");
        movie2.setMovie_duration(169);
        movie2.setMovie_actors("Matthew McConaughey, Anne Hathaway, Jessica Chastain");
        movie2.setMovie_age_req(8);
        movie2.setMovie_period_start(LocalDate.of(2025, 10, 1));
        movie2.setMovie_period_end(LocalDate.of(2025, 12, 1));
        movie2.setMovie_genre(Genre.ADVENTURE);
        movie2.setMovie_photo_href("https://m.media-amazon.com/images/M/MV5BYzdjMDAxZGItMjI2My00ODA1LTlkNzItOWFjMDU5ZDJlYWY3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg");

        movieRepository.save(movie2);

        Theater smallTheater = new Theater("Teater 1 (lille sal)", 20, 12);
        theaterRepository.save(smallTheater);

        Theater largeTheater = new Theater("Teater 2 (stor sal)", 25, 16);
        theaterRepository.save(largeTheater);

        Show show1 = new Show(movie1, smallTheater, LocalDateTime.of(2025, 10, 15, 19, 0), 120.0);
        showRepository.save(show1);

        Show show2 = new Show(movie1, largeTheater, LocalDateTime.of(2025, 10, 15, 21, 30), 120.0);
        showRepository.save(show2);

        Show show3 = new Show(movie1, smallTheater, LocalDateTime.of(2025, 10, 16, 19, 0), 120.0);
        showRepository.save(show3);

        Show show4 = new Show(movie2, largeTheater, LocalDateTime.of(2025, 10, 15, 18, 0), 130.0);
        showRepository.save(show4);

        Show show5 = new Show(movie2, smallTheater, LocalDateTime.of(2025, 10, 16, 21, 0), 130.0);
        showRepository.save(show5);

        System.out.println("InitData: Oprettet 2 movies, 2 teatre og 5 shows");


    }
}
