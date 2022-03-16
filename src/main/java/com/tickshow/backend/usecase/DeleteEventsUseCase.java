package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreEvent;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.entity.Event;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.repository.EventRepository;
import com.tickshow.backend.request.DeleteDataRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DeleteEventsUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteEventsUseCase.class);

    private final EventRepository eventRepository;
    private final DeleteDataRequest request;

    public PageableCoreEvent execute() throws EntityNotFoundException {
        for (Long id : request.getIds()) {
            Optional<Event> eventOptional = eventRepository.findById(id);

            if (!eventOptional.isPresent()) {
                log.error("Event not found");
                throw new EntityNotFoundException("Event not found");
            }

            Event event = eventOptional.get();

            eventRepository.delete(event);
        }

        Page<Event> eventPage = eventRepository.findAll(PageRequest.of(0, 10));

        return new PageableCoreEvent(
                eventPage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                eventPage.getTotalPages(),
                eventPage.getNumber()
        );
    }

    private CoreEvent convertToCoreEntity(Event event) {
        return new CoreEvent(
                event.getId(),
                event.getName(),
                event.getAddress(),
                event.getContact(),
                event.getPrice(),
                convertToCoreEntity(event.getLocation()),
                getImage(event.getFileName())
        );
    }

    private CoreLocation convertToCoreEntity(Location location) {
        return new CoreLocation(
                location.getId(),
                location.getLocation()
        );
    }

    @SneakyThrows
    private byte[] getImage(String fileName) {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService("events");
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}