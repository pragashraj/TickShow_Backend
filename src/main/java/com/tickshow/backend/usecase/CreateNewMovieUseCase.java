package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.model.coreEntity.CoreCast;
import com.tickshow.backend.model.coreEntity.CoreCrew;
import com.tickshow.backend.model.coreEntity.CoreGenre;
import com.tickshow.backend.model.entity.*;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.CreateNewMovieRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CreateNewMovieUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateNewMovieUseCase.class);

    private final MovieRepository movieRepository;
    private final RateRepository rateRepository;
    private final GenreRepository genreRepository;
    private final CastRepository castRepository;
    private final CrewRepository crewRepository;
    private final MovieShowTypeRepository movieShowTypeRepository;
    private final CreateNewMovieRequest request;

    public String execute() throws EntityNotFoundException {
        Rate rate = createRating();
        List<Genre> genres = getGenres();
        List<Cast> casts = createCasts();
        List<Crew> crews = createCrews();
        MovieShowType movieShowType = getMovieShowType();

        Movie movie = Movie.builder()
                .name(request.getName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .releaseDate(request.getReleaseDate())
                .experience(request.getExperience())
                .language(request.getLanguage())
                .rate(rate)
                .genres(genres)
                .casts(casts)
                .crews(crews)
                .movieShowType(movieShowType)
                .build();

        movieRepository.save(movie);

        return "New movie item create successfully";
    }

    private Rate createRating() {
        Rate rate = Rate.builder()
                .imdb(request.getImdb())
                .rotten(request.getRotten())
                .userRate((double) 0)
                .noOfRaters(0)
                .build();

        return rateRepository.save(rate);
    }

    private List<Cast> createCasts() {
        List<CoreCast> coreCasts = request.getCasts();

        List<Cast> casts = new ArrayList<>();

        for (CoreCast coreCast : coreCasts) {
            Cast castEntity = Cast.builder()
                    .artist(coreCast.getArtist())
                    .characterName(coreCast.getCharacterName())
                    .build();

            Cast cast = castRepository.save(castEntity);

            casts.add(cast);
        }

        return casts;
    }

    private List<Crew> createCrews() {
        List<CoreCrew> coreCrews = request.getCrews();

        List<Crew> crews = new ArrayList<>();

        for (CoreCrew coreCrew : coreCrews) {
            Crew crewEntity = Crew.builder()
                    .name(coreCrew.getName())
                    .role(coreCrew.getRole())
                    .build();

            Crew crew = crewRepository.save(crewEntity);

            crews.add(crew);
        }

        return crews;
    }

    private List<Genre> getGenres() {
        List<CoreGenre> coreGenres = request.getGenres();

        List<Genre> genres = new ArrayList<>();

        for (CoreGenre coreGenre : coreGenres) {
            Genre genre = genreRepository.findByGenre(coreGenre.getGenre());
            genres.add(genre);
        }

        return genres;
    }

    private MovieShowType getMovieShowType() throws EntityNotFoundException {
        MovieShowType movieShowType = movieShowTypeRepository.findByType(request.getMovieShowType());

        if (movieShowType == null) {
            log.error("Show type not found");
            throw new EntityNotFoundException("Show type not found");
        }

        return movieShowType;
    }
}