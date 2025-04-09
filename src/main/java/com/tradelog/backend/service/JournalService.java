package com.tradelog.backend.service;

import com.tradelog.backend.dto.journal.JournalRequest;
import com.tradelog.backend.dto.journal.JournalResponse;
import com.tradelog.backend.exception.ResourceNotFoundException;
import com.tradelog.backend.model.AiFeedback;
import com.tradelog.backend.model.Journal;
import com.tradelog.backend.model.User;
import com.tradelog.backend.repository.AiFeedbackRepository;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final AiFeedbackRepository aiFeedbackRepository;

    public Page<JournalResponse> getAllPublicJournals(Pageable pageable) {
        return journalRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageable)
                .map(this::convertToResponse);
    }

    public Page<JournalResponse> getMyJournals(Pageable pageable) {
        User currentUser = getCurrentUser();
        return journalRepository.findByUserOrderByCreatedAtDesc(currentUser, pageable)
                .map(this::convertToResponse);
    }

    public JournalResponse getJournalById(Long id) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));

        // 비공개 일지인 경우 본인만 볼 수 있음
        if (!journal.getIsPublic()) {
            User currentUser = getCurrentUser();
            if (!journal.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("비공개 일지는 작성자만 볼 수 있습니다.");
            }
        }

        return convertToResponse(journal);
    }

    @Transactional
    public JournalResponse createJournal(JournalRequest request) {
        User currentUser = getCurrentUser();

        Journal journal = Journal.builder()
                .user(currentUser)
                .stockSymbol(request.getStockSymbol())
                .buyDate(request.getBuyDate())
                .sellDate(request.getSellDate())
                .profitRate(request.getProfitRate())
                .strategyDescription(request.getStrategyDescription())
                .emotion(request.getEmotion())
                .isPublic(request.getIsPublic())
                .build();

        return convertToResponse(journalRepository.save(journal));
    }

    @Transactional
    public JournalResponse updateJournal(Long id, JournalRequest request) {
        User currentUser = getCurrentUser();
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));

        if (!journal.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("본인이 작성한 일지만 수정할 수 있습니다.");
        }

        journal.setStockSymbol(request.getStockSymbol());
        journal.setBuyDate(request.getBuyDate());
        journal.setSellDate(request.getSellDate());
        journal.setProfitRate(request.getProfitRate());
        journal.setStrategyDescription(request.getStrategyDescription());
        journal.setEmotion(request.getEmotion());
        journal.setIsPublic(request.getIsPublic());

        return convertToResponse(journalRepository.save(journal));
    }

    @Transactional
    public void deleteJournal(Long id) {
        User currentUser = getCurrentUser();
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));

        if (!journal.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("본인이 작성한 일지만 삭제할 수 있습니다.");
        }

        journalRepository.delete(journal);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private JournalResponse convertToResponse(Journal journal) {
        JournalResponse.AiFeedbackResponse aiFeedbackResponse = null;
        
        if (journal.getAiFeedback() != null) {
            AiFeedback aiFeedback = journal.getAiFeedback();
            aiFeedbackResponse = JournalResponse.AiFeedbackResponse.builder()
                    .id(aiFeedback.getId())
                    .content(aiFeedback.getContent())
                    .createdAt(aiFeedback.getCreatedAt())
                    .build();
        }
        
        return JournalResponse.builder()
                .id(journal.getId())
                .userId(journal.getUser().getId())
                .userNickname(journal.getUser().getNickname())
                .stockSymbol(journal.getStockSymbol())
                .buyDate(journal.getBuyDate())
                .sellDate(journal.getSellDate())
                .profitRate(journal.getProfitRate())
                .strategyDescription(journal.getStrategyDescription())
                .emotion(journal.getEmotion())
                .isPublic(journal.getIsPublic())
                .createdAt(journal.getCreatedAt())
                .updatedAt(journal.getUpdatedAt())
                .comments(journal.getComments().stream()
                        .map(comment -> com.tradelog.backend.dto.comment.CommentResponse.builder()
                                .id(comment.getId())
                                .journalId(comment.getJournal().getId())
                                .userId(comment.getUser().getId())
                                .userNickname(comment.getUser().getNickname())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .aiFeedback(aiFeedbackResponse)
                .build();
    }
} 