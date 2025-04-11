package com.tradelog.backend.repository;

import com.tradelog.backend.entity.AiFeedback;
import com.tradelog.backend.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiFeedbackRepository extends JpaRepository<AiFeedback, Long> {
    Optional<AiFeedback> findByJournal(Journal journal);
    boolean existsByJournal(Journal journal);
} 