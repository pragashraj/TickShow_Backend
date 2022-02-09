package com.tickshow.backend.model.coreEntity;

import com.tickshow.backend.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreMovie {
    private Long id;
    private String name;
    private String description;
    private String duration;
    private LocalDate releaseDate;
    private String experience;
    private Rate rate;
    private List<Genre> genres;
    private List<Cast> casts;
    private List<Crew> crews;
    private ShowType showType;
    private byte[] src;
}