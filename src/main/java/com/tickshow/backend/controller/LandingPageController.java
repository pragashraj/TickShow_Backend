package com.tickshow.backend.controller;

import com.tickshow.backend.repository.*;
import com.tickshow.backend.response.GetLandingPageContentsResponse;
import com.tickshow.backend.usecase.GetLandingPageContentsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/landingPage/")
public class LandingPageController {
    private static final Logger log = LoggerFactory.getLogger(LandingPageController.class);

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final EventRepository eventRepository;
    private final MovieShowTypeRepository movieShowTypeRepository;
    private final LocationRepository locationRepository;


    @Autowired
    public LandingPageController(MovieRepository movieRepository,
                                 TheatreRepository theatreRepository,
                                 EventRepository eventRepository,
                                 MovieShowTypeRepository movieShowTypeRepository,
                                 LocationRepository locationRepository
    ) {
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.eventRepository = eventRepository;
        this.movieShowTypeRepository = movieShowTypeRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("get-landingPage-contents")
    public ResponseEntity<?> getContents() {
        try {
            GetLandingPageContentsUseCase useCase = new GetLandingPageContentsUseCase(
                    movieRepository,
                    theatreRepository,
                    eventRepository,
                    movieShowTypeRepository,
                    locationRepository
            );
            GetLandingPageContentsResponse response = useCase.execute();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Unable to get contents, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}