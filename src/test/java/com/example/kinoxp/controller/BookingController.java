

package com.example.kinoxp.controller;

import com.example.kinoxp.model.*;
import com.example.kinoxp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowRepository showRepository;

    private User testUser;
    private Show testShow;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        testUser = new User("bookinguser", "password123", Role.CUSTOMER);
        testUser = userRepository.save(testUser);

        Movie movie = new Movie();
        movie.setMovie_title("Test Movie");
        movie.setMovie_description("Test");
        movie.setMovie_duration(120);
        movie.setMovie_actors("Test Actor");
        movie.setMovie_age_req(13);
        movie.setMovie_period_start(java.time.LocalDate.now());
        movie.setMovie_period_end(java.time.LocalDate.now().plusMonths(1));
        movie.setMovie_genre(Genre.ACTION);
        movie.setMovie_photo_href("http://test.jpg");
        movie = movieRepository.save(movie);

        Theater theater = new Theater("Test Theater", 10, 10);
        theater = theaterRepository.save(theater);

        testShow = new Show(movie, theater, LocalDateTime.now().plusDays(1), 100.0);
        testShow = showRepository.save(testShow);

        session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());
        session.setAttribute("username", testUser.getUsername());
        session.setAttribute("role", testUser.getRole().toString());
    }

    @Test
    void testGetAllShows() throws Exception {
        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetShow_Exists() throws Exception {
        mockMvc.perform(get("/api/shows/" + testShow.getShowId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showId").value(testShow.getShowId()));
    }

    @Test
    void testGetShow_NotFound() throws Exception {
        mockMvc.perform(get("/api/shows/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        String requestBody = String.format("""
                {
                    "showId": %d,
                    "seats": [
                        {"seatRow": 1, "seatNumber": 1},
                        {"seatRow": 1, "seatNumber": 2}
                    ]
                }
                """, testShow.getShowId());

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists())
                .andExpect(jsonPath("$.numberOfSeats").value(2));
    }

    @Test
    void testCreateBooking_NotLoggedIn() throws Exception {
        String requestBody = String.format("""
                {
                    "showId": %d,
                    "seats": [
                        {"seatRow": 1, "seatNumber": 1}
                    ]
                }
                """, testShow.getShowId());

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateBooking_DuplicateSeat() throws Exception {
        String requestBody = String.format("""
                {
                    "showId": %d,
                    "seats": [
                        {"seatRow": 5, "seatNumber": 5}
                    ]
                }
                """, testShow.getShowId());

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetBookedSeats() throws Exception {
        mockMvc.perform(get("/api/shows/" + testShow.getShowId() + "/booked-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
