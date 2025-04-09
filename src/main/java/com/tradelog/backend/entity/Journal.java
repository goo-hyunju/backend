package com.tradelog.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "journals")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private LocalDateTime buyDate;

    @Column(nullable = false)
    private LocalDateTime sellDate;

    @Column(nullable = false)
    private Double profitRate;

    @Column(columnDefinition = "TEXT")
    private String strategyDescription;

    @Column(columnDefinition = "TEXT")
    private String emotion;

    @Column(nullable = false)
    private Boolean isPublic = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
} 