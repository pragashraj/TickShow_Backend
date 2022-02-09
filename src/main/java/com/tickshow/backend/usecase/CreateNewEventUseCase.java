package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.entity.Event;
import com.tickshow.backend.model.entity.EventCategory;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.ShowType;
import com.tickshow.backend.repository.EventCategoryRepository;
import com.tickshow.backend.repository.EventRepository;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.ShowTypeRepository;
import com.tickshow.backend.request.CreateNewEventRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
public class CreateNewEventUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateNewEventUseCase.class);

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final ShowTypeRepository showTypeRepository;
    private final CreateNewEventRequest request;

    public String execute(MultipartFile file) throws EntityNotFoundException, FileStorageException {
        FileStorageService fileStorageService = new FileStorageService("events");

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/uploads/events/")
                .path(fileName)
                .toUriString();

        Location location = locationRepository.findByLocation(request.getLocation());

        if (location == null) {
            log.error("Location not found for: {}", request.getLocation());
            throw new EntityNotFoundException("Location not found");
        }

        EventCategory eventCategory = eventCategoryRepository.findByCategory(request.getEventCategory());

        if (eventCategory == null) {
            log.error("Category not found for: {}", request.getLocation());
            throw new EntityNotFoundException("Category not found");
        }

        ShowType showType = showTypeRepository.findByType(request.getShowType());

        if (showType == null) {
            log.error("Show type not found for: {}", request.getLocation());
            throw new EntityNotFoundException("Show type not found");
        }

        Event event = Event.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .price(new BigDecimal(request.getPrice()))
                .location(location)
                .eventCategory(eventCategory)
                .showType(showType)
                .fileName(fileDownloadUri)
                .build();

        eventRepository.save(event);

        return "New Event created successfully";
    }
}