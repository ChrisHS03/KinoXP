package com.example.kinoxp.repository;

import com.example.kinoxp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByBooking_BookingId(Integer bookingId);
    List<Seat> findByBooking_Show_ShowIdAndSeatRowsAndSeatNumber(Integer showId, int seatRows, int seatNumber);

}
