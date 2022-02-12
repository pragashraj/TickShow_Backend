package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findAllByRepliedOrderByDateTime(boolean isReplied, Pageable pageable);

    Contact findByIdAndName(Long id, String name);
}