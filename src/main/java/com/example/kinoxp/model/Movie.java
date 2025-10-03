package com.example.kinoxp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movie_id;
    private String movie_title;
    private String movie_description;
    private int movie_duration;
    private String movie_actors;
    private int movie_age_req;
    private LocalDate movie_period_start;
    private LocalDate movie_period_end;
    private Genre movie_genre;
    private String movie_photo_href;


    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public int getMovie_duration() {
        return movie_duration;
    }

    public void setMovie_duration(int movie_duration) {
        this.movie_duration = movie_duration;
    }

    public String getMovie_actors() {
        return movie_actors;
    }

    public void setMovie_actors(String movie_actors) {
        this.movie_actors = movie_actors;
    }

    public int getMovie_age_req() {
        return movie_age_req;
    }

    public void setMovie_age_req(int movie_age_req) {
        this.movie_age_req = movie_age_req;
    }

    public LocalDate getMovie_period_start() {
        return movie_period_start;
    }

    public void setMovie_period_start(LocalDate movie_period_start) {
        this.movie_period_start = movie_period_start;
    }

    public LocalDate getMovie_period_end() {
        return movie_period_end;
    }

    public void setMovie_period_end(LocalDate movie_period_end) {
        this.movie_period_end = movie_period_end;
    }


    public Genre getMovie_genre() {
        return movie_genre;
    }

    public void setMovie_genre(Genre movie_genre) {
        this.movie_genre = movie_genre;
    }

    public String getMovie_photo_href() {
        return movie_photo_href;
    }

    public void setMovie_photo_href(String movie_photo_href) {
        this.movie_photo_href = movie_photo_href;
    }

    public String getMovie_description() {
        return movie_description;
    }

    public void setMovie_description(String movie_description) {
        this.movie_description = movie_description;
    }
}
