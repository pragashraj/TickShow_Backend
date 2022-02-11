package com.tickshow.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generic-generator")
    @GenericGenerator(name = "generic-generator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "prefix", value = "AD"),
                    @org.hibernate.annotations.Parameter(name = "digits", value = "8"),
                    @org.hibernate.annotations.Parameter(name = "initial_id", value = "100000000"),
            },
            strategy = "com.tickshow.backend.utils.GenericIdGenerator")
    private String id;

    private String email;

    private String username;

    private String role;
}