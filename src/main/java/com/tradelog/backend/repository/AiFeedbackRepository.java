package com.tradelog.backend.repository;

import com.tradelog.backend.model.AiFeedback;
import com.tradelog.backend.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiFeedbackRepository extends JpaRepository<AiFeedback, Long> {
    Optional<AiFeedback> findByJournal(Journal journal);
    boolean existsByJournal(Journal journal);
} 