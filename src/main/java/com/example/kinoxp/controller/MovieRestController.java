package com.example.kinoxp.controller;

import com.example.kinoxp.model.Movie;
import com.example.kinoxp.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/movie/{id}")
    public Movie getMovie(@PathVariable int id){
        return movieRepository.findById(id).get();
    }

    @PostMapping("/createmovie")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createMovie(@RequestBody Movie movie, HttpSession session) {
        // Session auth
        String role = (String) session.getAttribute("role");
        if (!"EMPLOYEE".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }


    @PutMapping("/updatemovie/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable int id, @RequestBody Movie movie, HttpSession session) {

        String role = (String) session.getAttribute("role");
        if (!"EMPLOYEE".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

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
    public ResponseEntity<String> deleteMovie(@PathVariable int id, HttpSession session){
        // Session auth
        String role = (String) session.getAttribute("role");
        if (!"EMPLOYEE".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if(optionalMovie.isPresent()){
            movieRepository.deleteById(id);
            return ResponseEntity.ok("Movie deleted successfully");
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
