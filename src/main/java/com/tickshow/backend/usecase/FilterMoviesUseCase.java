package com.tickshow.backend.usecase;

import com.tickshow.backend.model.coreEntity.CoreMovie;
import com.tickshow.backend.model.entity.Genre;
import com.tickshow.backend.model.entity.Movie;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.GenreRepository;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.request.FilterMoviesRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FilterMoviesUseCase {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final FilterMoviesRequest request;

    public PageableCoreMovie execute() {
        List<Genre> genres = genreRepository.findAllByGenreIn(request.getGenres());
        Page<Movie> moviePage = movieRepository.findAllByLanguageInAndExperienceInAndGenresInOrderByReleaseDateDesc(
                request.getLanguages(),
                request.getExperiences(),
                genres,
                PageRequest.of(request.getPage(), 10)
        );

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
                movie.getShowType()
        );
    }
}