package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Event;
import com.tickshow.backend.model.entity.EventCategory;
import com.tickshow.backend.model.entity.ShowType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByEventCategoryIn(List<EventCategory> categories, Pageable pageable);

    Page<Event> findAllByShowType(ShowType type, Pageable pageable);

    Page<Event> findAllByNameLike(String name, Pageable pageable);
}