package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreCast;
import com.tickshow.backend.model.coreEntity.CoreCrew;
import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Cast;
import com.tickshow.backend.model.entity.Crew;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.request.DeleteDataRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DeleteMoviesUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteMoviesUseCase.class);

    private final MovieRepository movieRepository;
    private final DeleteDataRequest request;

    public PageableCoreMovie execute() throws EntityNotFoundException {
        for (Long id : request.getIds()) {
            Optional<Movie> movieOptional = movieRepository.findById(id);

            if (!movieOptional.isPresent()) {
                log.error("Movie not found for id: {}", id);
                throw new EntityNotFoundException("Movie not found");
            }

            Movie movie = movieOptional.get();

            movieRepository.delete(movie);
        }

        Page<Movie> moviePage = movieRepository.findAll(PageRequest.of(0, 10));

        return new PageableCoreMovie(
                moviePage.get().map(this::convertToCoreEntity).collect(Collectors.toList()),
                moviePage.getTotalPages(),
                moviePage.getNumber()
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

    @SneakyThrows
    private byte[] getImage(String path, String fileName) {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService(path);
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}