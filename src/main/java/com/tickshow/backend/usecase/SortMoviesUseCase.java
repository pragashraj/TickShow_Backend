package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.entity.ShowType;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.repository.ShowTypeRepository;
import com.tickshow.backend.request.SortMoviesRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SortMoviesUseCase {
    private static final Logger log = LoggerFactory.getLogger(SortMoviesUseCase.class);

    private final MovieRepository movieRepository;
    private final ShowTypeRepository showTypeRepository;
    private final SortMoviesRequest request;

    public PageableCoreMovie execute() throws EntityNotFoundException {
        ShowType showType = showTypeRepository.findByType(request.getSortBy());

        if (showType == null) {
            log.error("Show type not found for : {}", request.getSortBy());
            throw new EntityNotFoundException("Show type not found");
        }

        Page<Movie> moviePage = movieRepository.findAllByShowType(showType, PageRequest.of(
                request.getPage(),
                request.getSize()
        ));

        return new PageableCoreMovie(
                moviePage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
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
                movie.getCasts(),
                movie.getCrews(),
                movie.getShowType(),
                getImage(movie.getFileName())
        );
    }

    @SneakyThrows
    private byte[] getImage(String fileName) {
        if (fileName != null) {
            FileStorageService fileStorageService = new FileStorageService("movies");
            return fileStorageService.convert(fileName);
        }
        return null;
    }
}