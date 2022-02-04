package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreEvent;
import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.coreEntity.CoreTheatre;
import com.tickshow.backend.model.entity.*;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.response.GetLandingPageContentsResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetLandingPageContentsUseCase {

    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final EventRepository eventRepository;
    private final ShowTypeRepository showTypeRepository;
    private final LocationRepository locationRepository;

    public GetLandingPageContentsResponse execute() {
        Pageable pageable = PageRequest.of(1, 4);

        ShowType showType = showTypeRepository.findByType("Upcoming Movies");

        Page<Movie> moviePage = movieRepository.findAll(pageable);
        Page<Movie> upcomingMoviePage = movieRepository.findAllByMovieShowType(showType, pageable);
        Page<Theatre> theatrePage = theatreRepository.findAll(pageable);
        Page<Event> eventPage = eventRepository.findAll(pageable);

        PageableCoreMovie pageableCoreMovie = new PageableCoreMovie(
                moviePage.get().map(this::convertToCoreMovieEntity).collect(Collectors.toList()),
                moviePage.getTotalPages(),
                moviePage.getNumber()
        );

        PageableCoreMovie pageableCoreMovieUpcoming = new PageableCoreMovie(
                upcomingMoviePage.get().map(this::convertToCoreMovieEntity).collect(Collectors.toList()),
                upcomingMoviePage.getTotalPages(),
                upcomingMoviePage.getNumber()
        );

        PageableCoreTheatre pageableCoreTheatre = new PageableCoreTheatre(
                theatrePage.get().map(this::convertToCoreTheatreEntity).collect(Collectors.toList()),
                theatrePage.getTotalPages(),
                theatrePage.getNumber()
        );

        PageableCoreEvent pageableCoreEvent = new PageableCoreEvent(
                eventPage.get().map(this::convertToCoreEventEntity).collect(Collectors.toList()),
                eventPage.getTotalPages(),
                eventPage.getNumber()
        );

        List<Location> locations = locationRepository.findAll();
        List<CoreLocation> coreLocations = new ArrayList<>();

        for (Location location : locations) {
            CoreLocation coreLocation = convertToCoreLocationEntity(location);
            coreLocations.add(coreLocation);
        }

        return new GetLandingPageContentsResponse(
                pageableCoreMovie,
                pageableCoreMovieUpcoming,
                pageableCoreTheatre,
                pageableCoreEvent,
                coreLocations
        );
    }

    private CoreMovie convertToCoreMovieEntity(Movie movie) {
        return new CoreMovie(
                movie.getId(),
                movie.getName(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getReleaseDate(),
                movie.getExperience(),
                movie.getRate(),
                movie.getGenres(),
                movie.getCasts(),
                movie.getCrews(),
                movie.getShowType()
        );
    }

    private CoreTheatre convertToCoreTheatreEntity(Theatre theatre) {
        return new CoreTheatre(
                theatre.getId(),
                theatre.getName(),
                theatre.getAddress(),
                theatre.getContact(),
                theatre.getRate(),
                convertToCoreLocationEntity(theatre.getLocation())
        );
    }

    private CoreEvent convertToCoreEventEntity(Event event) {
        return new CoreEvent(
                event.getId(),
                event.getName(),
                event.getAddress(),
                event.getContact(),
                event.getPrice(),
                convertToCoreLocationEntity(event.getLocation())
        );
    }

    private CoreLocation convertToCoreLocationEntity(Location location) {
        return new CoreLocation(
                location.getId(),
                location.getLocation()
        );
    }
}