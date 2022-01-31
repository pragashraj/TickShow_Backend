package com.tickshow.backend.model.coreEntity;

import com.tickshow.backend.model.entity.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreTheatre {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private Rate rate;
}