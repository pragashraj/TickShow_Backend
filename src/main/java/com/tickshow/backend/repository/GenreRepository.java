package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAllByGenreIn(List<String> genres);
}