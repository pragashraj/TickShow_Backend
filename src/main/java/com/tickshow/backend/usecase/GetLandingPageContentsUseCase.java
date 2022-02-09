package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.*;
import com.tickshow.backend.model.entity.*;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.response.GetLandingPageContentsResponse;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
        Pageable pageable = PageRequest.of(0, 4);

        ShowType showType = showTypeRepository.findByType("Upcoming Movies");

        Page<Movie> moviePage = movieRepository.findAll(pageable);
        Page<Movie> upcomingMoviePage = movieRepository.findAllByShowType(showType, pageable);
        Page<Theatre> theatrePage = theatreRepository.findAll(pageable);
        Page<Event> eventPage = eventRepository.findAll(pageable);

        PageableCoreMovie pageableCoreMovie = new PageableCoreMovie(
                moviePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                moviePage.getTotalPages(),
                moviePage.getNumber()
        );

        PageableCoreMovie pageableCoreMovieUpcoming = new PageableCoreMovie(
                upcomingMoviePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                upcomingMoviePage.getTotalPages(),
                upcomingMoviePage.getNumber()
        );

        PageableCoreTheatre pageableCoreTheatre = new PageableCoreTheatre(
                theatrePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                theatrePage.getTotalPages(),
                theatrePage.getNumber()
        );

        PageableCoreEvent pageableCoreEvent = new PageableCoreEvent(
                eventPage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                eventPage.getTotalPages(),
                eventPage.getNumber()
        );

        List<Location> locations = locationRepository.findAll();
        List<CoreLocation> coreLocations = new ArrayList<>();

        for (Location location : locations) {
            CoreLocation coreLocation = convertToCoreEntity(location);
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

    private CoreMovie convertToCoreEntity(Movie movie) {
        return new CoreMovie(
                movie.getId(),
                movie.getName(),
                movie.getDescription(),
                movie.getDuration(),
                movie.getReleaseDate(),
                movie.getExperience(),
                movie.getRate(),
                movie.getGenres(),
                getCoreCasts(movie.getCasts()),
                getCoreCrews(movie.getCrews()),
                movie.getShowType(),
                getImage("movies", movie.getFileName())
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
                getImage("theatres", theatre.getFileName())
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
                getImage("events", event.getFileName())
        );
    }

    private CoreLocation convertToCoreEntity(Location location) {
        return new CoreLocation(
                location.getId(),
                location.getLocation()
        );
    }

    private CoreCast convertToCoreEntity(Cast cast) {
        return new CoreCast(
                cast.getId(),
                cast.getArtist(),
                cast.getCharacterName(),
                getImage("cast", cast.getFileName())
        );
    }

    private CoreCrew convertToCoreEntity(Crew crew) {
        return new CoreCrew(
                crew.getId(),
                crew.getName(),
                crew.getRole(),
                getImage("crew", crew.getFileName())
        );
    }

    private List<CoreCast> getCoreCasts(List<Cast> casts) {
        List<CoreCast> coreCasts = new ArrayList<>();

        for (Cast cast : casts) {
            CoreCast coreCast = convertToCoreEntity(cast);
            coreCasts.add(coreCast);
        }

        return coreCasts;
    }

    private List<CoreCrew> getCoreCrews(List<Crew> crews) {
        List<CoreCrew> coreCrews = new ArrayList<>();

        for (Crew crew : crews) {
            CoreCrew coreCrew = convertToCoreEntity(crew);
            coreCrews.add(coreCrew);
        }

        return coreCrews;
    }

    @SneakyThrows
    private byte[] getImage(String path, String fileName) {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService(path);
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}