package com.example.kinoxp.controller;

import com.example.kinoxp.model.Movie;
import com.example.kinoxp.model.Show;
import com.example.kinoxp.model.Theater;
import com.example.kinoxp.repository.MovieRepository;
import com.example.kinoxp.repository.ShowRepository;
import com.example.kinoxp.repository.TheaterRepository;
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

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)
@Transactional
@Rollback(true)
class ShowRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

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
    void testCreateShow_AsEmployee() throws Exception {
        Movie movie = movieRepository.findAll().get(0);
        Theater theater = theaterRepository.findAll().get(0);

        Show show = new Show(movie, theater, LocalDateTime.of(2025, 10, 15, 20, 0), 120.0);

        mockMvc.perform(post("/createshow")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(120.0));
    }

    @Test
    void testGetShowsByMovieId() throws Exception {
        Integer movieId = movieRepository.findAll().get(0).getMovie_id();

        mockMvc.perform(get("/shows/" + movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie.movie_title").exists());
    }

    @Test
    void testUpdateShow_AsEmployee() throws Exception {
        Show existing = showRepository.findAll().get(0);
        existing.setPrice(150.0);

        mockMvc.perform(put("/updateshow/" + existing.getShowId())
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(150.0));
    }

    @Test
    void testDeleteShow_AsEmployee() throws Exception {
        Show existing = showRepository.findAll().get(0);

        mockMvc.perform(delete("/deleteshow/" + existing.getShowId())
                        .session(employeeSession))
                .andExpect(status().isOk())
                .andExpect(content().string("Show deleted successfully"));
    }
}
