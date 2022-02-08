package com.tickshow.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generic-generator")
    @GenericGenerator(name = "generic-generator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "T"),
                    @org.hibernate.annotations.Parameter(name = "digits", value = "9"),
                    @org.hibernate.annotations.Parameter(name = "initial_id", value = "100000000"),
            },
            strategy = "com.tickshow.backend.utils.GenericIdGenerator")
    private String id;

    private String email;

    private int noOfFullTickets;

    private int noOfHalfTickets;

    @OneToOne
    private Movie movie;

    @OneToOne
    private Theatre theatre;

    @OneToOne
    private TimeSlot timeSlot;

    @OneToMany
    private List<Seat> seats;
}