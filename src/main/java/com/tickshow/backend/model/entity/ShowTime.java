package com.tickshow.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String movieName;

    private String theatreName;

    @OneToMany
    private List<TimeSlot> timeSlots;
}