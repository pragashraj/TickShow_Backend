package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.coreEntity.CoreTheatre;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.SortTheatresRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SortTheatresUseCase {
    private static final Logger log = LoggerFactory.getLogger(SortTheatresUseCase.class);

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final SortTheatresRequest request;

    public PageableCoreTheatre execute() throws EntityNotFoundException {
        Location location = locationRepository.findByLocation(request.getLocation());

        if (location == null) {
            log.error("Location not found for : {}", request.getLocation());
            throw new EntityNotFoundException("Location not found");
        }

        Page<Theatre> theatrePage = theatreRepository.findAllByLocation(
                location,
                PageRequest.of(request.getPage(), request.getSize())
        );

        return new PageableCoreTheatre(
                theatrePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                theatrePage.getTotalPages(),
                theatrePage.getNumber()
        );
    }

    private CoreTheatre convertToCoreEntity(Theatre theatre) {
        return new CoreTheatre(
                theatre.getId(),
                theatre.getName(),
                theatre.getAddress(),
                theatre.getContact(),
                theatre.getRate(),
                convertToCoreEntity(theatre.getLocation())
        );
    }

    private CoreLocation convertToCoreEntity(Location location) {
        return new CoreLocation(
                location.getId(),
                location.getLocation()
        );
    }
}