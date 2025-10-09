

package com.example.kinoxp.controller;

import com.example.kinoxp.model.Booking;
import com.example.kinoxp.model.Seat;
import com.example.kinoxp.model.Show;
import com.example.kinoxp.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @GetMapping("/shows")
    public ResponseEntity<List<Show>> getAllShows() {
        List<Show> shows = bookingService.getAllShows();
        return ResponseEntity.ok(shows);
    }


    @GetMapping("/shows/{id}")
    public ResponseEntity<?> getShow(@PathVariable Integer id) {
        return bookingService.getShowById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/shows/{id}/booked-seats")
    public ResponseEntity<List<Seat>> getBookedSeats(@PathVariable Integer id) {
        List<Seat> bookedSeats = bookingService.getBookedSeatsForShow(id);
        return ResponseEntity.ok(bookedSeats);
    }


    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            HttpSession session
    ) {
        try {

            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(401).body("You must be signed in");
            }


            Booking booking = bookingService.createBooking(
                    userId,
                    request.getShowId(),
                    request.getSeats()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("bookingId", booking.getBookingId());
            response.put("message", "Booking created");
            response.put("numberOfSeats", booking.getNumberOfSeats());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Booking failed: " + e.getMessage());
        }
    }


    public static class BookingRequest {
        private Integer showId;
        private List<BookingService.SeatRequest> seats;

        public BookingRequest() {}

        public Integer getShowId() {
            return showId;
        }

        public void setShowId(Integer showId) {
            this.showId = showId;
        }

        public List<BookingService.SeatRequest> getSeats() {
            return seats;
        }

        public void setSeats(List<BookingService.SeatRequest> seats) {
            this.seats = seats;
        }
    }
}