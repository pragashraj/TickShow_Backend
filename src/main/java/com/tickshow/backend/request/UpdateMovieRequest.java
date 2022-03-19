package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMovieRequest {
    private Long id;
    private String name;
    private String duration;
    private LocalDate releaseDate;
    private String experience;
    private int page;
}