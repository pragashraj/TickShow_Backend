package com.tickshow.backend.model.coreEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreContact {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private LocalDateTime dateTime;
    private String message;
    private boolean isReplied;
}