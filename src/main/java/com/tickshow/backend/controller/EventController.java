package com.tickshow.backend.controller;

import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.repository.EventCategoryRepository;
import com.tickshow.backend.repository.EventRepository;
import com.tickshow.backend.request.FilterEventsRequest;
import com.tickshow.backend.usecase.FilterEventsUseCase;
import com.tickshow.backend.usecase.GetEventsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/events/")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventRepository eventRepository;
    private final EventCategoryRepository eventCategoryRepository;

    @Autowired
    public EventController(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
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
}