package com.example.kinoxp.controller;

import com.example.kinoxp.model.Genre;
import com.example.kinoxp.model.Movie;
import com.example.kinoxp.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"} // sørg for, at h2init.sql indeholder movie testdata
)
@Transactional
@Rollback(true)
class MovieRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession employeeSession;
    private MockHttpSession customerSession;

    @BeforeEach
    void setupSessions() {
        employeeSession = new MockHttpSession();
        employeeSession.setAttribute("role", "EMPLOYEE");

        customerSession = new MockHttpSession();
        customerSession.setAttribute("role", "CUSTOMER");
    }

    @Test
    void testCreateMovie_AsEmployee() throws Exception {
        Movie movie = new Movie();
        movie.setMovie_title("Inception");
        movie.setMovie_description("Mind-bending sci-fi");
        movie.setMovie_duration(148);
        movie.setMovie_actors("Leonardo DiCaprio");
        movie.setMovie_age_req(13);
        movie.setMovie_period_start(java.time.LocalDate.of(2025, 10, 1));
        movie.setMovie_period_end(java.time.LocalDate.of(2025, 12, 31));

        movie.setMovie_photo_href("https://example.com/inception.jpg");

        mockMvc.perform(post("/createmovie")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movie_title").value("Inception"));
    }

    @Test
    void testCreateMovie_ForbiddenForCustomer() throws Exception {
        Movie movie = new Movie();
        movie.setMovie_title("Forbidden Movie");

        mockMvc.perform(post("/createmovie")
                        .session(customerSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie_title").exists());
    }

    @Test
    void testGetMovieById() throws Exception {
        int id = movieRepository.findAll().get(0).getMovie_id();

        mockMvc.perform(get("/movie/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie_title").exists());
    }

    @Test
    void testUpdateMovie_AsEmployee() throws Exception {
        Movie existing = movieRepository.findAll().get(0);
        existing.setMovie_title("Inception Updated");

        mockMvc.perform(put("/updatemovie/" + existing.getMovie_id())
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie_title").value("Inception Updated"));
    }

    @Test
    void testDeleteMovie_AsEmployee() throws Exception {
        int id = movieRepository.findAll().get(0).getMovie_id();

        mockMvc.perform(delete("/deletemovie/" + id)
                        .session(employeeSession))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie deleted successfully"));
    }

    @Test
    void testDeleteMovie_ForbiddenForCustomer() throws Exception {
        int id = movieRepository.findAll().get(0).getMovie_id();

        mockMvc.perform(delete("/deletemovie/" + id)
                        .session(customerSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteMovie_NotFound() throws Exception {
        Long nonExistingId = 9999L;

        mockMvc.perform(delete("/deletemovie/" + nonExistingId)
                        .session(employeeSession))
                .andExpect(status().isNotFound());
    }
}
