

package com.example.kinoxp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id", referencedColumnName = "show_id", nullable = false)
    private Show show;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false)
    private int numberOfSeats;

    public Booking() {}
    public Booking(Integer booking_id, User user, LocalDateTime bookingTime, int numberOfSeats) {
        this.booking_id = booking_id;
        this.user = user;
        this.bookingTime = bookingTime;
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
