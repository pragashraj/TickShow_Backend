package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Genre;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.entity.ShowType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findAllByLanguageInAndExperienceInAndGenresInOrderByReleaseDateDesc(
            List<String> languages, List<String> experiences, List<Genre> genres, Pageable pageable
    );

    Page<Movie> findAllByShowType(ShowType showType, Pageable pageable);

    Page<Movie> findAllByNameLike(String name, Pageable pageable);

    List<Movie> findAllByNameInAndExperience(List<String> names, String experience);

    Movie findByName(String name);
}