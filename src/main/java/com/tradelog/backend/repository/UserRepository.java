package com.tradelog.backend.repository;

import com.tradelog.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
} 