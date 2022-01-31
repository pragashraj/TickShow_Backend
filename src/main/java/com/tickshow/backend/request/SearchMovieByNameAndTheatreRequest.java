package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMovieByNameAndTheatreRequest {
    private String name;
    private LocalDate date;
    private String city;
    private String experience;
    private int page;
}