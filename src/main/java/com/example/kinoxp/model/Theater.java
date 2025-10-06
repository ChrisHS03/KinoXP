

package com.example.kinoxp.model;

import jakarta.persistence.*;

@Entity
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer theater_id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int seatRows;

    @Column(nullable = false)
    private int seatsPerRow;

    public Theater() {}
    public Theater(Integer id, String name, int seatRows, int seatsPerRow) {
        this.theater_id = id;
        this.name = name;
        this.seatRows = seatRows;
        this.seatsPerRow = seatsPerRow;
    }

    public Integer getId() {
        return theater_id;
    }

    public void setId(Integer id) {
        this.theater_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTheater_id() {
        return theater_id;
    }

    public void setTheater_id(Integer theater_id) {
        this.theater_id = theater_id;
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
