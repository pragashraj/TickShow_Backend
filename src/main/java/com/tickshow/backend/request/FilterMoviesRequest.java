package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterMoviesRequest {
    private List<String> languages = new ArrayList<>();
    private List<String> experiences = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private int page;
    private int size;
}