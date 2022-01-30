package com.tickshow.backend.model.coreEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreCast {
    private Long id;
    private String artist;
    private String characterName;
}