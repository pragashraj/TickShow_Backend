package com.tickshow.backend.model.pageableEntity;

import com.tickshow.backend.model.coreEntity.CoreMovie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreMovie {
    private List<CoreMovie> movies = new ArrayList<>();
    private int total;
    private int current;
}