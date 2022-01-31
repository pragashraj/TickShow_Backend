package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    @Query("select s.movieName from ShowTime s where s.movieName like %?1% and s.theatreName in ?2")
    List<String> getMovieNamesByMovieNameInAndTheatreNameIn(String movieName, List<String> theatreName);
}