package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.pageableEntity.PageableCoreContact;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.CreateNewEventRequest;
import com.tickshow.backend.request.CreateNewMovieRequest;
import com.tickshow.backend.request.CreateNewTheatreRequest;
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
}