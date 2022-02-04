package com.tickshow.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Lob
    private String description;

    private String duration;

    private LocalDate releaseDate;

    private String experience;

    private String language;

    @OneToOne
    private Rate rate;

    @ManyToMany
    private List<Genre> genres;

    @OneToMany
    private List<Cast> casts;

    @OneToMany
    private List<Crew> crews;

    @OneToOne
    private ShowType showType;
}