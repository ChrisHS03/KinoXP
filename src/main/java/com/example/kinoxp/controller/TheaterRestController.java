package com.example.kinoxp.controller;

import com.example.kinoxp.model.Show;
import com.example.kinoxp.model.Theater;
import com.example.kinoxp.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class TheaterRestController {

    @Autowired
    TheaterRepository theaterRepository;

    @GetMapping("/theater/{id}")
    public Theater getTheaterById(@PathVariable int id) {
        return theaterRepository.findById(id).get();
    }

}
