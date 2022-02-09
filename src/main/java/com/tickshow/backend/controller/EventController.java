package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.repository.EventCategoryRepository;
import com.tickshow.backend.repository.EventRepository;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.ShowTypeRepository;
import com.tickshow.backend.request.CreateNewEventRequest;
import com.tickshow.backend.request.FilterEventsRequest;
import com.tickshow.backend.request.SortEventsRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.usecase.CreateNewEventUseCase;
import com.tickshow.backend.usecase.FilterEventsUseCase;
import com.tickshow.backend.usecase.GetEventsUseCase;
import com.tickshow.backend.usecase.SortEventsUseCase;
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
@RequestMapping("/api/events/")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ShowTypeRepository showTypeRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public EventController(EventRepository eventRepository,
                           EventCategoryRepository eventCategoryRepository,
                           ShowTypeRepository showTypeRepository,
                           LocationRepository locationRepository
    ) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.showTypeRepository = showTypeRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("get-events")
    public ResponseEntity<?> getEvents(@RequestParam int page, @RequestParam int size) {
        try {
            GetEventsUseCase useCase = new GetEventsUseCase(eventRepository);
            PageableCoreEvent pageableCoreEvent = useCase.execute(page, size);
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (Exception e) {
            log.error("Unable to get events, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("filter-events")
    public ResponseEntity<?> filterEvents(@RequestBody FilterEventsRequest request) {
        try {
            FilterEventsUseCase useCase = new FilterEventsUseCase(
                    eventRepository,
                    eventCategoryRepository,
                    request
            );
            PageableCoreEvent pageableCoreEvent = useCase.execute();
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (Exception e) {
            log.error("Unable to filter events, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("sort-events")
    public ResponseEntity<?> sortEvents(@RequestBody SortEventsRequest request) {
        try {
            SortEventsUseCase useCase = new SortEventsUseCase(
                    eventRepository,
                    showTypeRepository,
                    request
            );
            PageableCoreEvent pageableCoreEvent = useCase.execute();
            return ResponseEntity.ok(pageableCoreEvent);
        } catch (Exception e) {
            log.error("Unable to sort events, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping(value = "create-event", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createEvent(@RequestPart("request") CreateNewEventRequest request, @RequestPart("file") MultipartFile file) {
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
}