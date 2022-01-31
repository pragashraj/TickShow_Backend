package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.request.SearchMovieByNameAndTheatreRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SearchMovieByNameAndTheatreUseCase {
    private static final Logger log = LoggerFactory.getLogger(SearchMovieByNameAndTheatreUseCase.class);

    private final MovieRepository movieRepository;
    private final LocationRepository locationRepository;
    private final SearchMovieByNameAndTheatreRequest request;

    public PageableCoreMovie execute() throws EntityNotFoundException {
        Location location = locationRepository.findByLocation(request.getCity());

        if (location == null) {
            log.error("Location not found");
            throw new EntityNotFoundException("Location not found");
        }

        Page<Movie> moviePage = movieRepository.findAllByNameLikeAndExperience(request.getName(), request.getExperience());

        return new PageableCoreMovie(
                moviePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
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