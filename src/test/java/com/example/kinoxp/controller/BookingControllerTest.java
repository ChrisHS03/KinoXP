package com.example.kinoxp.controller;

import com.example.kinoxp.model.*;
import com.example.kinoxp.service.BookingService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BookingControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;
    private Show testShow;
    private User testUser;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testUser = new User("bookinguser", "password123", Role.CUSTOMER);
        testUser.setId(1);

        Movie movie = new Movie();
        movie.setMovie_id(1);
        movie.setMovie_title("Test Movie");

        Theater theater = new Theater("Test Theater", 10, 10);
        theater.setTheaterId(1);

        testShow = new Show(movie, theater, LocalDateTime.now().plusDays(1), 100.0);
        testShow.setShowId(1);

        testBooking = new Booking();
        testBooking.setBookingId(1);
        testBooking.setUser(testUser);
        testBooking.setShow(testShow);
        testBooking.setNumberOfSeats(2);
        testBooking.setBookingTime(LocalDateTime.now());

        session = new MockHttpSession();
        session.setAttribute("userId", 1);
        session.setAttribute("username", "bookinguser");
        session.setAttribute("role", "CUSTOMER");
    }

    @Test
    void testGetAllShows_ReturnsList() throws Exception {
        when(bookingService.getAllShows()).thenReturn(List.of(testShow));

        mockMvc.perform(get("/api/shows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].showId").value(1));
    }

    @Test
    void testGetShow_Exists() throws Exception {
        when(bookingService.getShowById(1)).thenReturn(Optional.of(testShow));

        mockMvc.perform(get("/api/shows/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.showId").value(1));
    }

    @Test
    void testGetShow_NotFound() throws Exception {
        when(bookingService.getShowById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/shows/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        when(bookingService.createBooking(anyInt(), anyInt(), any()))
                .thenReturn(testBooking);

        String requestBody = """
                {
                    "showId": 1,
                    "seats": [
                        {"seatRow": 1, "seatNumber": 1},
                        {"seatRow": 1, "seatNumber": 2}
                    ]
                }
                """;

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.numberOfSeats").value(2))
                .andExpect(jsonPath("$.message").value("Booking created"));
    }

    @Test
    void testCreateBooking_UnauthorizedWhenNoSession() throws Exception {
        String requestBody = """
                {
                    "showId": 1,
                    "seats": [{"seatRow": 1, "seatNumber": 1}]
                }
                """;

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("You must be signed in"));
    }

    @Test
    void testGetBookedSeats_ReturnsEmptyList() throws Exception {
        when(bookingService.getBookedSeatsForShow(1)).thenReturn(List.of());

        mockMvc.perform(get("/api/shows/1/booked-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
