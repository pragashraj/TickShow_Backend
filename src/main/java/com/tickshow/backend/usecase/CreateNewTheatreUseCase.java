package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Rate;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.repository.LocationRepository;
import com.tickshow.backend.repository.RateRepository;
import com.tickshow.backend.repository.TheatreRepository;
import com.tickshow.backend.request.CreateNewTheatreRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@AllArgsConstructor
public class CreateNewTheatreUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateNewTheatreUseCase.class);

    private final TheatreRepository theatreRepository;
    private final LocationRepository locationRepository;
    private final RateRepository rateRepository;
    private final CreateNewTheatreRequest request;

    public String execute(MultipartFile file) throws EntityNotFoundException, FileStorageException {
        FileStorageService fileStorageService = new FileStorageService("theatres");

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/uploads/theatres/")
                .path(fileName)
                .toUriString();

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
                .fileName(fileDownloadUri)
                .build();

        theatreRepository.save(theatre);

        return "New Theatre created successfully";
    }

    private Rate createRating() {
        Rate rate = Rate.builder().userRate((double) 0).noOfRaters(0).build();
        return rateRepository.save(rate);
    }
}