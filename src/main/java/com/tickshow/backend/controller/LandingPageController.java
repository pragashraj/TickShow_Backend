package com.tickshow.backend.controller;

import com.tickshow.backend.exception.AlreadyExistException;
import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.SearchMovieByNameAndTheatreRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.response.GetLandingPageContentsResponse;
import com.tickshow.backend.usecase.GetLandingPageContentsUseCase;
import com.tickshow.backend.usecase.SearchMovieByNameAndTheatreUseCase;
import com.tickshow.backend.usecase.SubscribeToNewsletterUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/landingPage/")
public class LandingPageController {
    private static final Logger log = LoggerFactory.getLogger(LandingPageController.class);

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final EventRepository eventRepository;
    private final ShowTypeRepository showTypeRepository;
    private final LocationRepository locationRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SubscriberRepository subscriberRepository;


    @Autowired
    public LandingPageController(MovieRepository movieRepository,
                                 TheatreRepository theatreRepository,
                                 EventRepository eventRepository,
                                 ShowTypeRepository showTypeRepository,
                                 LocationRepository locationRepository,
                                 ShowTimeRepository showTimeRepository,
                                 SubscriberRepository subscriberRepository
    ) {
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.eventRepository = eventRepository;
        this.showTypeRepository = showTypeRepository;
        this.locationRepository = locationRepository;
        this.showTimeRepository = showTimeRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @GetMapping("get-landingPage-contents")
    public ResponseEntity<?> getContents() {
        try {
            GetLandingPageContentsUseCase useCase = new GetLandingPageContentsUseCase(
                    movieRepository,
                    theatreRepository,
                    eventRepository,
                    showTypeRepository,
                    locationRepository
            );
            GetLandingPageContentsResponse response = useCase.execute();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Unable to get contents, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("search-movie")
    public ResponseEntity<?> searchMovie(@RequestBody SearchMovieByNameAndTheatreRequest request) {
        try {
            SearchMovieByNameAndTheatreUseCase useCase = new SearchMovieByNameAndTheatreUseCase(
                    movieRepository,
                    locationRepository,
                    theatreRepository,
                    showTimeRepository,
                    request
            );
            List<Movie> movies = useCase.execute();
            return ResponseEntity.ok(movies);
        } catch (EntityNotFoundException e) {
            log.error("Unable to search movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to search movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("subscribe-newsletter/{email}")
    public ResponseEntity<?> subscribeToNewsletter(@PathVariable String email) {
        try {
            SubscribeToNewsletterUseCase useCase = new SubscribeToNewsletterUseCase(subscriberRepository);
            String response = useCase.execute(email);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (AlreadyExistException e) {
            log.error("Unable to subscribe, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to subscribe, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}