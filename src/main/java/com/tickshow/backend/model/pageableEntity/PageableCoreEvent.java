package com.tickshow.backend.model.pageableEntity;

import com.tickshow.backend.model.coreEntity.CoreEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableCoreEvent {
    private List<CoreEvent> events = new ArrayList<>();
    private int total;
    private int current;
}