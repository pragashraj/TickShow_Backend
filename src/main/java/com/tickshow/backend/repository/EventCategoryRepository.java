package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    EventCategory findByCategory(String category);
}