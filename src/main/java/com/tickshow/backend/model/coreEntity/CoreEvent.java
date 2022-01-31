package com.tickshow.backend.model.coreEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreEvent {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private BigDecimal price;
    private CoreLocation location;
}