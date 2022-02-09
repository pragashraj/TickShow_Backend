package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.RateRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.CreateNewTheatreRequest;
import com.tickshow.backend.request.SortTheatresRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.usecase.CreateNewTheatreUseCase;
import com.tickshow.backend.usecase.GetTheatresUseCase;
import com.tickshow.backend.usecase.SortTheatresUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/theatres/")
public class TheatreController {
    private static final Logger log = LoggerFactory.getLogger(TheatreController.class);

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final RateRepository rateRepository;

    @Autowired
    public TheatreController(TheatreRepository theatreRepository,
                             LocationRepository locationRepository,
                             RateRepository rateRepository
    ) {
        this.theatreRepository = theatreRepository;
        this.locationRepository = locationRepository;
        this.rateRepository = rateRepository;
    }

    @GetMapping("get-theatres")
    public ResponseEntity<?> getTheatres(@RequestParam int page, @RequestParam int size) {
        try {
            GetTheatresUseCase useCase = new GetTheatresUseCase(theatreRepository);
            PageableCoreTheatre pageableCoreTheatre = useCase.execute(page, size);
            return ResponseEntity.ok(pageableCoreTheatre);
        } catch (Exception e) {
            log.error("Unable to get theatres, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("sort-theatres")
    public ResponseEntity<?> sortTheatres(@RequestBody SortTheatresRequest request) {
        try {
            SortTheatresUseCase useCase = new SortTheatresUseCase(
                    theatreRepository,
                    locationRepository,
                    request
            );
            PageableCoreTheatre pageableCoreTheatre = useCase.execute();
            return ResponseEntity.ok(pageableCoreTheatre);
        } catch (EntityNotFoundException e) {
            log.error("Unable to sort theatres, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to sort theatres, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping(value = "create-theatre", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createTheatre(@RequestPart("request") CreateNewTheatreRequest request, @RequestPart("file") MultipartFile file) {
        try {
            CreateNewTheatreUseCase useCase = new CreateNewTheatreUseCase(
                    theatreRepository,
                    locationRepository,
                    rateRepository,
                    request
            );
            String response = useCase.execute(file);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | FileStorageException e) {
            log.error("Unable to create a new theatre, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to create a new theatre, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}