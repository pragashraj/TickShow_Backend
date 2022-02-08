package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketsRequest {
    private String email;
    private String movieName;
    private String theatre;
    private String timeSlot;
    private int noOfFullTickets;
    private int noOfHalfTickets;
}