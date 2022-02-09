package com.tickshow.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String contact;

    private BigDecimal price;

    private String fileName;

    @OneToOne
    private Location location;

    @OneToOne
    private EventCategory eventCategory;

    @OneToOne
    private ShowType showType;
}