package com.example.kinoxp.controller;

import com.example.kinoxp.model.Movie;
import com.example.kinoxp.model.Show;
import com.example.kinoxp.model.Theater;
import com.example.kinoxp.repository.ShowRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShowRestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ShowRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowRepository showRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie;
    private Theater theater;
    private Show show;
    private MockHttpSession employeeSession;
    private MockHttpSession customerSession;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setMovie_id(1);
        movie.setMovie_title("Inception");

        theater = new Theater();
        theater.setTheaterId(1);
        theater.setName("Big");

        show = new Show(movie, theater, LocalDateTime.of(2025, 10, 15, 20, 0), 120.0);
        show.setShowId(1);

        employeeSession = new MockHttpSession();
        employeeSession.setAttribute("role", "EMPLOYEE");

        customerSession = new MockHttpSession();
        customerSession.setAttribute("role", "CUSTOMER");
    }

    @Test
    void testGetShowsById_ReturnsListOfShows() throws Exception {
        when(showRepository.findByMovieId(1)).thenReturn(List.of(show));

        mockMvc.perform(get("/shows/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].showId").value(1))
                .andExpect(jsonPath("$[0].movie.movie_title").value("Inception"));
    }

    @Test
    void testCreateShow_ForbiddenForCustomer() throws Exception {
        mockMvc.perform(post("/createshow")
                        .session(customerSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateShow_CreatedForEmployee() throws Exception {
        when(showRepository.save(any(Show.class))).thenReturn(show);

        mockMvc.perform(post("/createshow")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.showId").value(1))
                .andExpect(jsonPath("$.movie.movie_title").value("Inception"));
    }

    @Test
    void testUpdateShow_UpdatesExistingShow() throws Exception {
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        when(showRepository.save(any(Show.class))).thenReturn(show);

        show.setPrice(150.0);

        mockMvc.perform(put("/updateshow/1")
                        .session(employeeSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(show)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(150.0));
    }

    @Test
    void testDeleteShow_SuccessForEmployee() throws Exception {
        when(showRepository.findById(1)).thenReturn(Optional.of(show));

        mockMvc.perform(delete("/deleteshow/1")
                        .session(employeeSession))
                .andExpect(status().isOk())
                .andExpect(content().string("Show deleted successfully"));
    }

    @Test
    void testDeleteShow_ForbiddenForCustomer() throws Exception {
        mockMvc.perform(delete("/deleteshow/1")
                        .session(customerSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteShow_NotFound() throws Exception {
        when(showRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/deleteshow/1")
                        .session(employeeSession))
                .andExpect(status().isNotFound());
    }
}
