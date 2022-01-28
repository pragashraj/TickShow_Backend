package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetMoviesUseCase {
    private final MovieRepository movieRepository;

    public PageableCoreMovie execute(int page) {
        Page<Movie> moviePage = movieRepository.findAll(PageRequest.of(page, 10));

        return new PageableCoreMovie(
                moviePage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                moviePage.getTotalPages(),
                moviePage.getNumber()
        );
    }

    private CoreMovie convertToCoreEntity(Movie movie) {
        return new CoreMovie(
                movie.getId(),
                movie.getName(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getReleaseDate(),
                movie.getExperience(),
                movie.getRate(),
                movie.getGenres(),
                movie.getCasts(),
                movie.getCrews(),
                movie.getMovieShowType()
        );
    }
}