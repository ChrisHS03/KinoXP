

package com.example.kinoxp.model;

import jakarta.persistence.*;

@Entity
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Integer theaterId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int seatRows;

    @Column(nullable = false)
    private int seatsPerRow;

    public Theater() {}
    public Theater(String name, int seatRows, int seatsPerRow) {
        this.name = name;
        this.seatRows = seatRows;
        this.seatsPerRow = seatsPerRow;
    }

    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatRows() {
        return seatRows;
    }

    public void setSeatRows(int seatRows) {
        this.seatRows = seatRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
