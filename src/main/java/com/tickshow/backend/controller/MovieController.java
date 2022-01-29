package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.GenreRepository;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.repository.MovieShowTypeRepository;
import com.tickshow.backend.request.FilterMoviesRequest;
import com.tickshow.backend.usecase.FilterMoviesUseCase;
import com.tickshow.backend.usecase.GetMoviesUseCase;
import com.tickshow.backend.usecase.SortMoviesUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/movies/")
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieShowTypeRepository movieShowTypeRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository,
                           GenreRepository genreRepository,
                           MovieShowTypeRepository movieShowTypeRepository
    ) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieShowTypeRepository = movieShowTypeRepository;
    }

    @GetMapping("get-movies")
    public ResponseEntity<?> getMovies(@RequestParam int page) {
        try {
            GetMoviesUseCase useCase = new GetMoviesUseCase(movieRepository);
            PageableCoreMovie pageableCoreMovie = useCase.execute(page);
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (Exception e) {
            log.error("Unable to get movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("filter-movies")
    public ResponseEntity<?> filterMovies(@RequestBody FilterMoviesRequest request) {
        try {
            FilterMoviesUseCase useCase = new FilterMoviesUseCase(
                    movieRepository,
                    genreRepository,
                    request
            );
            PageableCoreMovie pageableCoreMovie = useCase.execute();
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (Exception e) {
            log.error("Unable to filter movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("sort-movies/{sortBy}/{page}")
    public ResponseEntity<?> sortMovies(@PathVariable String sortBy, @PathVariable int page) {
        try {
            SortMoviesUseCase useCase = new SortMoviesUseCase(
                    movieRepository,
                    movieShowTypeRepository
            );
            PageableCoreMovie pageableCoreMovie = useCase.execute(sortBy, page);
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (EntityNotFoundException e) {
            log.error("Unable to filter movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to filter movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}