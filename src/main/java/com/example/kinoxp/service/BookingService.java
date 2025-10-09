

package com.example.kinoxp.service;

import com.example.kinoxp.model.*;
import com.example.kinoxp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Show> getAllShows() {
        return showRepository.findAll();
    }


    public Optional<Show> getShowById(Integer showId) {
        return showRepository.findById(showId);
    }


    public boolean isSeatAvailable(Integer showId, int seatRow, int seatNumber) {
        List<Seat> existingSeats = seatRepository.findByBooking_Show_ShowIdAndSeatRowsAndSeatNumber(
                showId, seatRow, seatNumber);
        return existingSeats.isEmpty();
    }





    @Transactional
    public Booking createBooking(Integer userId, Integer showId, List<SeatRequest> seatRequests) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Bruger ikke fundet"));

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show ikke fundet"));


        for (SeatRequest seatRequest : seatRequests) {
            if (!isSeatAvailable(showId, seatRequest.getSeatRow(), seatRequest.getSeatNumber())) {
                throw new IllegalStateException(
                        "Række " + seatRequest.getSeatRow() +
                                ", nummer " + seatRequest.getSeatNumber() + " er allerede reserveret");
            }
        }


        Booking booking = new Booking(user, show, LocalDateTime.now(), seatRequests.size());
        booking = bookingRepository.save(booking);


        for (SeatRequest seatRequest : seatRequests) {
            Seat seat = new Seat(booking, seatRequest.getSeatRow(), seatRequest.getSeatNumber());
            seatRepository.save(seat);
        }

        return booking;
    }


    public List<Seat> getBookedSeatsForShow(Integer showId) {
        List<Booking> bookings = bookingRepository.findByShow_ShowId(showId);
        return bookings.stream()
                .flatMap(booking -> seatRepository.findByBooking_BookingId(booking.getBookingId()).stream())
                .toList();
    }


    // Inner class til at modtage sæde-requests
    public static class SeatRequest {
        private int seatRow;
        private int seatNumber;

        public SeatRequest() {}
        public SeatRequest(int seatRow, int seatNumber) {
            this.seatRow = seatRow;
            this.seatNumber = seatNumber;
        }

        public int getSeatRow() {
            return seatRow;
        }

        public void setSeatRow(int seatRow) {
            this.seatRow = seatRow;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public void setSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
        }
    }
}