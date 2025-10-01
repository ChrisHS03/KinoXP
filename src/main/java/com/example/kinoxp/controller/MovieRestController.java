package com.example.kinoxp.controller;

import com.example.kinoxp.model.Movie;
import com.example.kinoxp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class MovieRestController {

    @Autowired
    MovieRepository movieRepository;

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @PostMapping("/createmovie")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @PutMapping("/updatemovie/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable int id, @RequestBody Movie movie) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            movie.setMovie_id(optionalMovie.get().getMovie_id());
            movieRepository.save(movie);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletemovie/{id}")
        public ResponseEntity<String> deleteMovie(@PathVariable int id){
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isPresent()){
            movieRepository.deleteById(id);
            return ResponseEntity.ok("Movie deleted successfully");
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
