package com.tickshow.backend.controller;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.CreateNewMovieRequest;
import com.tickshow.backend.request.FilterMoviesRequest;
import com.tickshow.backend.request.SortMoviesRequest;
import com.tickshow.backend.response.ApiResponse;
import com.tickshow.backend.usecase.*;
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
    private final ShowTypeRepository showTypeRepository;
    private final RateRepository rateRepository;
    private final CastRepository castRepository;
    private final CrewRepository crewRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository,
                           GenreRepository genreRepository,
                           ShowTypeRepository showTypeRepository,
                           RateRepository rateRepository,
                           CastRepository castRepository,
                           CrewRepository crewRepository
    ) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.showTypeRepository = showTypeRepository;
        this.rateRepository = rateRepository;
        this.castRepository = castRepository;
        this.crewRepository = crewRepository;
    }

    @GetMapping("get-movies")
    public ResponseEntity<?> getMovies(@RequestParam int page, @RequestParam int size) {
        try {
            GetMoviesUseCase useCase = new GetMoviesUseCase(movieRepository);
            PageableCoreMovie pageableCoreMovie = useCase.execute(page, size);
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

    @PostMapping("sort-movies")
    public ResponseEntity<?> sortMovies(@RequestBody SortMoviesRequest request) {
        try {
            SortMoviesUseCase useCase = new SortMoviesUseCase(
                    movieRepository,
                    showTypeRepository,
                    request
            );
            PageableCoreMovie pageableCoreMovie = useCase.execute();
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (EntityNotFoundException e) {
            log.error("Unable to sort movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to sort movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @GetMapping("search-movie")
    public ResponseEntity<?> searchMovie(@RequestParam String name, @RequestParam int page) {
        try {
            SearchMovieUseCase useCase = new SearchMovieUseCase(movieRepository);
            PageableCoreMovie pageableCoreMovie = useCase.execute(name, page);
            return ResponseEntity.ok(pageableCoreMovie);
        } catch (Exception e) {
            log.error("Unable to search movies, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }

    @PostMapping("create-new-movie")
    public ResponseEntity<?> createNewMovie(@RequestBody CreateNewMovieRequest request) {
        try {
            CreateNewMovieUseCase useCase = new CreateNewMovieUseCase(
                    movieRepository,
                    rateRepository,
                    genreRepository,
                    castRepository,
                    crewRepository,
                    showTypeRepository,
                    request
            );
            String response = useCase.execute();
            ApiResponse apiResponse = new ApiResponse(true, response);
            return ResponseEntity.ok(apiResponse);
        } catch (EntityNotFoundException e) {
            log.error("Unable to create new movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to create new movie, cause: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "server error, please try again");
        }
    }
}