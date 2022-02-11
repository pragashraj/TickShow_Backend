package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByIdAndEmail(String id, String email);
}