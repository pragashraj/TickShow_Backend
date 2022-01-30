package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.entity.MovieShowType;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.repository.MovieShowTypeRepository;
import com.tickshow.backend.request.SortMoviesRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SortMoviesUseCase {
    private static final Logger log = LoggerFactory.getLogger(SortMoviesUseCase.class);

    private final MovieRepository movieRepository;
    private final MovieShowTypeRepository movieShowTypeRepository;
    private final SortMoviesRequest request;

    public PageableCoreMovie execute() throws EntityNotFoundException {
        MovieShowType movieShowType = movieShowTypeRepository.findByType(request.getSortBy());

        if (movieShowType == null) {
            log.error("Show type not found for : {}", request.getSortBy());
            throw new EntityNotFoundException("Show type not found");
        }

        Page<Movie> moviePage = movieRepository.findAllByMovieShowType(movieShowType, PageRequest.of(
                request.getPage(),
                request.getSize()
        ));

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