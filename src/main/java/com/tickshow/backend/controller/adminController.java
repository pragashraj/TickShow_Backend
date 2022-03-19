package com.tickshow.backend.controller;

import com.tickshow.backend.exception.ContentCreationException;
import com.tickshow.backend.exception.DispatcherException;
import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.pageableEntity.PageableCoreContact;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.*;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.transport.EmailService;
import com.tickshow.backend.transport.templates.MessageResponseTemplate;
import com.tickshow.backend.usecase.*;
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
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin/")
public class adminController {
    private static final Logger log = LoggerFactory.getLogger(adminController.class);

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ShowTypeRepository showTypeRepository;
    private final RateRepository rateRepository;
    private final CastRepository castRepository;
    private final CrewRepository crewRepository;
    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final EventRepository eventRepository;
    private final ContactRepository contactRepository;
    private final MessageResponseTemplate messageResponseTemplate;
    private final EmailService emailService;

    @Autowired
    public adminController(MovieRepository movieRepository,
                           GenreRepository genreRepository,
                           ShowTypeRepository showTypeRepository,
                           RateRepository rateRepository,
                           CastRepository castRepository,
                           CrewRepository crewRepository,
                           TheatreRepository theatreRepository,
                           LocationRepository locationRepository,
                           EventCategoryRepository eventCategoryRepository,
                           EventRepository eventRepository,
                           ContactRepository contactRepository,
                           MessageResponseTemplate messageResponseTemplate,
                           EmailService emailService
    ) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.showTypeRepository = showTypeRepository;
        this.rateRepository = rateRepository;
        this.castRepository = castRepository;
        this.crewRepository = crewRepository;
        this.theatreRepository = theatreRepository;
        this.locationRepository = locationRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventRepository = eventRepository;
        this.contactRepository = contactRepository;
        this.messageResponseTemplate = messageResponseTemplate;
        this.emailService = emailService;
    }


    @PostMapping(value = "create-new-movie", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createNewMovie(@RequestPart("request") CreateNewMovieRequest request,
                                            @RequestPart("file") MultipartFile file) {
        try {
            CreateNewMovieUseCase useCase = new CreateNewMovieUseCase(
                    movieRepository,
                    rateRepository,
                    genreRepository,
                    castRepository,
                    crewRepository,
                    showTypeRepository,
                    request
            );
            String response = useCase.execute(file);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | FileStorageException e) {
            log.error("Unable to create new movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to create new movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping(value = "create-new-theatre", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createTheatre(@RequestPart("request") CreateNewTheatreRequest request,
                                           @RequestPart("file") MultipartFile file) {
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

    @PostMapping(value = "create-new-event", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createEvent(@RequestPart("request") CreateNewEventRequest request,
                                         @RequestPart("file") MultipartFile file) {
        try {
            CreateNewEventUseCase useCase = new CreateNewEventUseCase(
                    eventRepository,
                    locationRepository,
                    eventCategoryRepository,
                    showTypeRepository,
                    request
            );
            String response = useCase.execute(file);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException | FileStorageException e) {
            log.error("Unable to create a new event, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to create a new event, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("get-messages/{page}")
    public ResponseEntity<?> getMessages(@PathVariable int page) {
        try {
            GetMessagesUseCase useCase = new GetMessagesUseCase(contactRepository);
            PageableCoreContact pageableCoreContact = useCase.execute(page);
            return ResponseEntity.ok(pageableCoreContact);
        } catch (Exception e) {
            log.error("Unable to get user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("get-messages-by-isReplied")
    public ResponseEntity<?> getMessagesByIsReplied(@RequestParam boolean isReplied, @RequestParam int page) {
        try {
            GetMessagesByIsRepliedUseCase useCase = new GetMessagesByIsRepliedUseCase(contactRepository);
            PageableCoreContact pageableCoreContact = useCase.execute(isReplied, page);
            return ResponseEntity.ok(pageableCoreContact);
        } catch (Exception e) {
            log.error("Unable to get user messages by isReplied, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("response-to-user-message")
    public ResponseEntity<?> responseToMessage(@RequestBody ResponseToMessageRequest request) {
        try {
            ResponseToMessageUseCase useCase = new ResponseToMessageUseCase(
                    contactRepository,
                    request,
                    messageResponseTemplate,
                    emailService
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to sent response to user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception | DispatcherException | ContentCreationException e) {
            log.error("Unable to sent response to user messages, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("delete-user-messages")
    public ResponseEntity<?> deleteUserMessages(@RequestBody DeleteDataRequest request) {
        try {
            DeleteUserMessageUseCase useCase = new DeleteUserMessageUseCase(contactRepository, request);
            PageableCoreContact pageableCoreContact = useCase.execute();
            return ResponseEntity.ok(pageableCoreContact);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete user message, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to delete user message, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("search-movie")
    public ResponseEntity<?> searchMovie(@RequestParam String name, @RequestParam int page) {
        try {
            SearchMovieUseCase useCase = new SearchMovieUseCase(movieRepository);
            PageableCoreMovie pageableCoreMovie = useCase.execute(name, page);
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (Exception e) {
            log.error("Unable to search movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("search-event")
    public ResponseEntity<?> searchEvent(@RequestParam String name, @RequestParam int page) {
        try {
            SearchEventUseCase useCase = new SearchEventUseCase(eventRepository);
            PageableCoreEvent pageableCoreEvent = useCase.execute(name, page);
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (Exception e) {
            log.error("Unable to search event, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("search-theatre")
    public ResponseEntity<?> searchTheatre(@RequestParam String name, @RequestParam int page) {
        try {
            SearchTheatreUseCase useCase = new SearchTheatreUseCase(theatreRepository);
            PageableCoreTheatre pageableCoreTheatre = useCase.execute(name, page);
            return ResponseEntity.ok(pageableCoreTheatre);
        } catch (Exception e) {
            log.error("Unable to search theatre, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("delete-movie")
    public ResponseEntity<?> deleteMovies(@RequestBody DeleteDataRequest request) {
        try {
            DeleteMoviesUseCase useCase = new DeleteMoviesUseCase(movieRepository, request);
            PageableCoreMovie pageableCoreMovie = useCase.execute();
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to delete movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("delete-event")
    public ResponseEntity<?> deleteEvents(@RequestBody DeleteDataRequest request) {
        try {
            DeleteEventsUseCase useCase = new DeleteEventsUseCase(eventRepository, request);
            PageableCoreEvent pageableCoreEvent = useCase.execute();
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete events, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to delete events, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("delete-theatre")
    public ResponseEntity<?> deleteTheatres(@RequestBody DeleteDataRequest request) {
        try {
            DeleteTheatresUseCase useCase = new DeleteTheatresUseCase(theatreRepository, request);
            PageableCoreTheatre pageableCoreTheatre = useCase.execute();
            return ResponseEntity.ok(pageableCoreTheatre);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete theatres, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to delete theatres, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("update-event")
    public ResponseEntity<?> updateEvent(@RequestBody UpdateEventRequest request) {
        try {
            UpdateEventUseCase useCase = new UpdateEventUseCase(eventRepository, locationRepository, request);
            PageableCoreEvent pageableCoreEvent = useCase.execute();
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update an event, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update an event, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("update-theatre")
    public ResponseEntity<?> updateTheatre(@RequestBody UpdateTheatreRequest request) {
        try {
            UpdateTheatreUseCase useCase = new UpdateTheatreUseCase(theatreRepository, locationRepository, request);
            PageableCoreTheatre pageableCoreTheatre = useCase.execute();
            return ResponseEntity.ok(pageableCoreTheatre);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update a theatre, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update a theatre, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("update-movie")
    public ResponseEntity<?> updateMovie(@RequestBody UpdateMovieRequest request) {
        try {
            UpdateMovieUseCase useCase = new UpdateMovieUseCase(movieRepository, request);
            PageableCoreMovie pageableCoreMovie = useCase.execute();
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update a movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update a movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}