package com.example.kinoxp.controller;

import com.example.kinoxp.model.Genre;
import com.example.kinoxp.model.Movie;
import com.example.kinoxp.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MovieRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MovieRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie;
    private MockHttpSession employeeSession;
    private MockHttpSession customerSession;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setMovie_id(1);
        movie.setMovie_title("Inception");
        movie.setMovie_description("Mind-bending sci-fi");
        movie.setMovie_duration(148);
        movie.setMovie_actors("Leonardo DiCaprio");
        movie.setMovie_age_req(13);
        movie.setMovie_period_start(LocalDate.of(2025, 10, 1));
        movie.setMovie_period_end(LocalDate.of(2025, 12, 31));
        movie.setMovie_genre(Genre.ACTION);
        movie.setMovie_photo_href("https://example.com/inception.jpg");

        employeeSession = new MockHttpSession();
        employeeSession.setAttribute("role", "EMPLOYEE");

        customerSession = new MockHttpSession();
        customerSession.setAttribute("role", "CUSTOMER");
    }

    @Test
    void testGetMovies_ReturnsListOfMovies() throws Exception {
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie_title").value("Inception"));
    }

    @Test
    void testGetMovieById_ReturnsMovie() throws Exception {
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/movie/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie_title").value("Inception"));
    }

    @Test
    void testCreateMovie_ForbiddenForCustomer() throws Exception {
        mockMvc.perform(post("/createmovie")
                        .session(customerSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateMovie_CreatedForEmployee() throws Exception {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/createmovie")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movie_title").value("Inception"));
    }

    @Test
    void testUpdateMovie_UpdatesExistingMovie() throws Exception {
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        movie.setMovie_title("Inception Updated");

        mockMvc.perform(put("/updatemovie/1")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie_title").value("Inception Updated"));
    }

    @Test
    void testDeleteMovie_SuccessForEmployee() throws Exception {
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));

        mockMvc.perform(delete("/deletemovie/1")
                        .session(employeeSession))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie deleted successfully"));
    }

    @Test
    void testDeleteMovie_ForbiddenForCustomer() throws Exception {
        mockMvc.perform(delete("/deletemovie/1")
                        .session(customerSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteMovie_NotFound() throws Exception {
        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/deletemovie/1")
                        .session(employeeSession))
                .andExpect(status().isNotFound());
    }

}
