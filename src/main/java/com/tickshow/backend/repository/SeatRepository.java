package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findByRowAndSeatNumber(String row, int seatNumber);
}