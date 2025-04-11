package com.tradelog.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate buyDate;

    @Column
    private LocalDate sellDate;

    @Column(nullable = false)
    private Double profitRate;

    @Column(columnDefinition = "TEXT")
    private String strategyDescription;

    @Column
    private String emotion;

    @Column(nullable = false)
    private Boolean isPublic;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    private AiFeedback aiFeedback;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
} 