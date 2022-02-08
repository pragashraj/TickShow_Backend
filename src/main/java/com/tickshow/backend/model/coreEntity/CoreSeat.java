package com.tickshow.backend.model.coreEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreSeat {
    private String row;
    private int seatNumber;
}