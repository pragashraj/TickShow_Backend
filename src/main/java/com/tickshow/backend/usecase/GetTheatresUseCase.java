package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.coreEntity.CoreTheatre;
import com.tickshow.backend.model.entity.Location;
import com.tickshow.backend.model.entity.Theatre;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.TheatreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetTheatresUseCase {
    private final TheatreRepository theatreRepository;

    public PageableCoreTheatre execute(int page, int size) {
        Page<Theatre> theatrePage = theatreRepository.findAll(PageRequest.of(page, size));

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