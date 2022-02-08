package com.tickshow.backend.request;

import com.tickshow.backend.model.coreEntity.CoreSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketsRequest {
    private String email;
    private String movieName;
    private String theatre;
    private LocalTime timeSlot;
    private int noOfFullTickets;
    private int noOfHalfTickets;
    private List<CoreSeat> seats = new ArrayList<>();
}