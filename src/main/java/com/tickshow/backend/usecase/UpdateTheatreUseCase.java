package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.coreEntity.CoreTheatre;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.UpdateTheatreRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UpdateTheatreUseCase {
    private static final Logger log = LoggerFactory.getLogger(UpdateTheatreUseCase.class);

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final UpdateTheatreRequest request;

    public PageableCoreTheatre execute() throws EntityNotFoundException {
        Optional<Theatre> theatreOptional = theatreRepository.findById(request.getId());

        if (!theatreOptional.isPresent()) {
            log.error("Theatre not found for id: {}", request.getId());
            throw new EntityNotFoundException("Theatre not found");
        }

        Theatre theatre = theatreOptional.get();

        Location location = locationRepository.findByLocation(request.getLocation());

        if (location == null) {
            log.error("Location entity not found for given location: {}", request.getLocation());
            throw new EntityNotFoundException("Location entity not found");
        }

        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setContact(request.getContact());
        theatre.setLocation(location);

        theatreRepository.save(theatre);

        Page<Theatre> theatrePage = theatreRepository.findAll(PageRequest.of(request.getPage(), 10));

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
                convertToCoreEntity(theatre.getLocation()),
                null
        );
    }

    private CoreLocation convertToCoreEntity(Location location) {
        return new CoreLocation(
                location.getId(),
                location.getLocation()
        );
    }
}