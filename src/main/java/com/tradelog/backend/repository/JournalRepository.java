package com.tradelog.backend.repository;

import com.tradelog.backend.model.Journal;
import com.tradelog.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    
    Page<Journal> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Journal> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    @Query("SELECT j FROM Journal j WHERE j.isPublic = true AND LOWER(j.stockSymbol) LIKE LOWER(CONCAT('%', :symbol, '%'))")
    Page<Journal> findPublicJournalsByStockSymbol(String symbol, Pageable pageable);
    
    List<Journal> findTop5ByUserOrderByCreatedAtDesc(User user);
} 