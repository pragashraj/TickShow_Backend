package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreEvent;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.entity.Event;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.ShowType;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.repository.EventRepository;
import com.tickshow.backend.repository.ShowTypeRepository;
import com.tickshow.backend.request.SortEventsRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SortEventsUseCase {
    private final EventRepository eventRepository;
    private final ShowTypeRepository showTypeRepository;
    private final SortEventsRequest request;

    public PageableCoreEvent execute() {
        ShowType showType = showTypeRepository.findByType(request.getSortBy());

        Page<Event> eventPage = eventRepository.findAllByShowType(
                showType,
                PageRequest.of(request.getPage(), request.getSize())
        );

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