package com.tickshow.backend.controller;

import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.repository.MovieRepository;
import com.tickshow.backend.usecase.GetMoviesUseCase;
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

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
}