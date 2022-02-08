package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.BookTicketsRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class BookTicketsUseCase {
    private static final Logger log = LoggerFactory.getLogger(BookTicketsUseCase.class);

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final BookTicketsRequest request;

    public String execute() throws EntityNotFoundException {
        Movie movie = movieRepository.findByName(request.getMovieName());

        if (movie == null) {
            log.error("Movie with name {} not found", request.getMovieName());
            throw new EntityNotFoundException("Movie not found");
        }

        Theatre theatre = theatreRepository.findByName(request.getTheatre());

        if (theatre == null) {
            log.error("Theatre with name {} not found", request.getTheatre());
            throw new EntityNotFoundException("Theatre not found");
        }

        return "Tickets successfully booked, and check your mail for confirmation";
    }
}