package com.tradelog.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_feedbacks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AiFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false, unique = true)
    private Journal journal;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "strength_points", columnDefinition = "TEXT")
    private String strengthPoints;

    @Column(name = "weakness_points", columnDefinition = "TEXT")
    private String weaknessPoints;

    @Column(name = "improvement_suggestions", columnDefinition = "TEXT")
    private String improvementSuggestions;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
} 