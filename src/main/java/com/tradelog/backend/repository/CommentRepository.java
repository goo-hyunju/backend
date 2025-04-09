package com.tradelog.backend.repository;

import com.tradelog.backend.model.Comment;
import com.tradelog.backend.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByJournalOrderByCreatedAtDesc(Journal journal, Pageable pageable);
} 