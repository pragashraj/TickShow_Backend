package com.tickshow.backend.usecase;

import com.tickshow.backend.exception.EntityNotFoundException;
import com.tickshow.backend.exception.FileStorageException;
import com.tickshow.backend.model.coreEntity.CoreCast;
import com.tickshow.backend.model.coreEntity.CoreCrew;
import com.tickshow.backend.model.coreEntity.CoreGenre;
import com.tickshow.backend.model.entity.*;
import com.tickshow.backend.repository.*;
import com.tickshow.backend.request.CreateNewMovieRequest;
import com.tickshow.backend.utils.FileStorageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    private final ShowTypeRepository showTypeRepository;
    private final CreateNewMovieRequest request;

    public String execute(MultipartFile file) throws EntityNotFoundException, FileStorageException {
        FileStorageService fileStorageService = new FileStorageService("movies");

        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/uploads/movies/")
                .path(fileName)
                .toUriString();

        Rate rate = createRating();
        List<Genre> genres = getGenres();
        List<Cast> casts = createCasts();
        List<Crew> crews = createCrews();
        ShowType showType = getShowType();

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
                .showType(showType)
                .fileName(fileDownloadUri)
                .build();

        movieRepository.save(movie);

        return "New movie created successfully";
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

    private ShowType getShowType() throws EntityNotFoundException {
        ShowType showType = showTypeRepository.findByType(request.getShowType());

        if (showType == null) {
            log.error("Show type not found");
            throw new EntityNotFoundException("Show type not found");
        }

        return showType;
    }
}