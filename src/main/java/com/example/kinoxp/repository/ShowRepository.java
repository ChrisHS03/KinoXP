

package com.example.kinoxp.repository;

import com.example.kinoxp.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s WHERE s.movie.movie_id = :movieId")
    List<Show> findByMovieId(@Param("movieId") int movieId);
    List<Show> findByShowTimeAfter(LocalDateTime time);
}
