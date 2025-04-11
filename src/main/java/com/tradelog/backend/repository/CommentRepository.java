package com.tradelog.backend.repository;

import com.tradelog.backend.entity.Comment;
import com.tradelog.backend.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByJournalOrderByCreatedAtDesc(Journal journal);
} 