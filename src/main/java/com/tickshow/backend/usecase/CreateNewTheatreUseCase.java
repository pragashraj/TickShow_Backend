package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Rate;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.RateRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.CreateNewTheatreRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class CreateNewTheatreUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateNewTheatreUseCase.class);

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final RateRepository rateRepository;
    private final CreateNewTheatreRequest request;

    public String execute() throws EntityNotFoundException {
        Location location = locationRepository.findByLocation(request.getLocation());

        if (location == null) {
            log.error("Location not found for: {}", request.getLocation());
            throw new EntityNotFoundException("Location not found");
        }

        Rate rate = createRating();

        Theatre theatre = Theatre.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .rate(rate)
                .location(location)
                .build();

        theatreRepository.save(theatre);

        return "New Theatre created successfully";
    }

    private Rate createRating() {
        Rate rate = Rate.builder().userRate((double) 0).noOfRaters(0).build();
        return rateRepository.save(rate);
    }
}