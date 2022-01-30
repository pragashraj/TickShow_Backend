package com.tickshow.backend.request;

import com.tickshow.backend.model.coreEntity.CoreCast;
import com.tickshow.backend.model.coreEntity.CoreCrew;
import com.tickshow.backend.model.coreEntity.CoreGenre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewMovieRequest {
    private String name;
    private String description;
    private String duration;
    private LocalDate releaseDate;
    private String experience;
    private String language;
    private Double imdb;
    private Double rotten;
    private List<CoreGenre> genres;
    private List<CoreCast> casts;
    private List<CoreCrew> crews;
    private String movieShowType;
}