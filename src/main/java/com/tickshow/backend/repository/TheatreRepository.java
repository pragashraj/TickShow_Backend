package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    @Query(value = "select t.name from Theatre t where t.location =? 1", nativeQuery = true)
    List<String> findAllTheatreNamesByLocation(Location location);

    Page<Theatre> findAllByLocation(Location location, Pageable pageable);
}