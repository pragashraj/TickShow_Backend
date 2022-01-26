package com.tickshow.backend.repository;

import com.tickshow.backend.model.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<UserAuthentication, Long> {
    UserAuthentication findByEmail(String email);
}