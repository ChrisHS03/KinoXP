

package com.example.kinoxp.repository;

import com.example.kinoxp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser_Id(Integer userId);
    @Query("SELECT b FROM Booking b WHERE b.show.showId = :showId")
    List<Booking> findByShow_ShowId(@Param("showId") Integer showId);
}
