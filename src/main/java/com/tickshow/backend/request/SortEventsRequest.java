package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortEventsRequest {
    private String sortBy;
    private int page;
    private int size;
}