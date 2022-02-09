package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;

@AllArgsConstructor
public class SearchMovieUseCase {
    private final MovieRepository movieRepository;

    public PageableCoreMovie execute(String name, int page) {
        Page<Movie> moviePage = movieRepository.findAllByNameLike(name, PageRequest.of(page, 10));

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
