package com.tickshow.backend.model.pageableEntity;

import com.tickshow.backend.model.coreEntity.CoreTheatre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreTheatre {
    private List<CoreTheatre> theatres = new ArrayList<>();
    private int total;
    private int current;
}