package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.coreEntity.CoreTheatre;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.TheatreRepository;
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
public class DeleteTheatresUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteTheatresUseCase.class);

    private final TheatreRepository theatreRepository;
    private final DeleteDataRequest request;

    public PageableCoreTheatre execute() throws EntityNotFoundException {
        for (Long id : request.getIds()) {
            Optional<Theatre> theatreOptional = theatreRepository.findById(id);

            if (!theatreOptional.isPresent()) {
                log.error("Theatre not found for id: {}", id);
                throw new EntityNotFoundException("Theatre not found");
            }

            Theatre theatre = theatreOptional.get();

            theatreRepository.delete(theatre);
        }

        Page<Theatre> theatrePage = theatreRepository.findAll(PageRequest.of(0, 10));

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
                getImage(theatre.getFileName())
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
            FileStorageService fileStorageService = new FileStorageService("theatres");
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}