package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateEventRequest {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private String price;
    private String location;
    private int page;
}