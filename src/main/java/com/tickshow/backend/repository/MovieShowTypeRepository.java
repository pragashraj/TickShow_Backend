package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.MovieShowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieShowTypeRepository extends JpaRepository<MovieShowType, Long> {
}