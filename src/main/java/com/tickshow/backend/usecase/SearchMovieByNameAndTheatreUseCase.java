package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.repository.ShowTimeRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.SearchMovieByNameAndTheatreRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
public class SearchMovieByNameAndTheatreUseCase {
    private static final Logger log = LoggerFactory.getLogger(SearchMovieByNameAndTheatreUseCase.class);

    private final MovieRepository movieRepository;
    private final LocationRepository locationRepository;
    private final TheatreRepository theatreRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SearchMovieByNameAndTheatreRequest request;

    public List<Movie> execute() throws EntityNotFoundException {
        Location location = locationRepository.findByLocation(request.getCity());

        if (location == null) {
            log.error("Location not found");
            throw new EntityNotFoundException("Location not found");
        }

        List<String> theatres = theatreRepository.findAllTheatreNamesByLocation(location);

        List<String> movieNames = showTimeRepository.getMovieNamesByMovieNameInAndTheatreNameIn(
                request.getName(),
                theatres
        );

        return movieRepository.findAllByNameInAndExperience(movieNames, request.getExperience());
    }
}