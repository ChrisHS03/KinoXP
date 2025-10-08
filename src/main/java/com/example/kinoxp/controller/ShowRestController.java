package com.example.kinoxp.controller;

import com.example.kinoxp.model.Movie;
import com.example.kinoxp.model.Show;
import com.example.kinoxp.model.Theater;
import com.example.kinoxp.repository.MovieRepository;
import com.example.kinoxp.repository.ShowRepository;
import com.example.kinoxp.repository.TheaterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ShowRestController {

    @Autowired
    ShowRepository showRepository;


    @GetMapping("/shows/{id}")
    public List<Show> getShowsById(@PathVariable int id) {
        return showRepository.findByMovieId(id);
    }
    @PostMapping("/createshow")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createShow(@RequestBody Show show, HttpSession session) {
        try {
            String role = (String) session.getAttribute("role");
            if (!"EMPLOYEE".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            Show savedShow = showRepository.save(show);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedShow);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fejl i createShow: " + e.getMessage());
        }
    }

    @PutMapping("/updateshow/{id}")
    public ResponseEntity<?> updateShow(@PathVariable int id, @RequestBody Show show, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (!"EMPLOYEE".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Optional<Show> optionalShow = showRepository.findById(id);
        if (optionalShow.isPresent()) {
            show.setShowId(optionalShow.get().getShowId());
            showRepository.save(show);
            return ResponseEntity.ok(show);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteshow/{id}")
    public ResponseEntity<String> deleteShow(@PathVariable int id, HttpSession session) {
        // Session auth
        String role = (String) session.getAttribute("role");
        if (!"EMPLOYEE".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Optional<Show> optionalShow = showRepository.findById(id);
        if (optionalShow.isPresent()) {
            showRepository.deleteById(id);
            return ResponseEntity.ok("Show deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
