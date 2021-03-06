package com.tickshow.backend.response;

import com.tickshow.backend.model.coreEntity.CoreLocation;
import com.tickshow.backend.model.pageableEntity.PageableCoreEvent;
import com.tickshow.backend.model.pageableEntity.PageableCoreMovie;
import com.tickshow.backend.model.pageableEntity.PageableCoreTheatre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLandingPageContentsResponse {
    private PageableCoreMovie moviePage;
    private PageableCoreMovie upcomingMoviePage;
    private PageableCoreTheatre theatrePage;
    private PageableCoreEvent eventPage;
    private List<CoreLocation> locations;
}