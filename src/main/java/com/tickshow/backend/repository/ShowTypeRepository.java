package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.ShowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowTypeRepository extends JpaRepository<ShowType, Long> {
    ShowType findByType(String type);
}