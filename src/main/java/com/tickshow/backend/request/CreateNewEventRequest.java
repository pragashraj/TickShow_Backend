package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewEventRequest {
    private String name;
    private String address;
    private String contact;
    private String price;
    private String location;
    private String eventCategory;
    private String showType;
}