package com.tradelog.backend.service;

import com.tradelog.backend.dto.journal.JournalResponse;
import com.tradelog.backend.exception.ResourceNotFoundException;
import com.tradelog.backend.model.AiFeedback;
import com.tradelog.backend.model.Journal;
import com.tradelog.backend.model.User;
import com.tradelog.backend.repository.AiFeedbackRepository;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiFeedbackService {

    private final AiFeedbackRepository aiFeedbackRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final JournalService journalService;

    @Transactional
    public JournalResponse generateAiFeedback(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        User currentUser = getCurrentUser();

        // 본인 일지만 AI 피드백 받을 수 있음
        if (!journal.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("본인의 일지에 대해서만 AI 피드백을 받을 수 있습니다.");
        }

        // 이미 피드백이 존재하는지 확인
        if (aiFeedbackRepository.existsByJournal(journal)) {
            // 이미 존재하면 그대로 반환
            return journalService.getJournalById(journalId);
        }

        // AI 피드백 생성 로직
        String feedbackContent = generateFeedbackContent(journal);

        AiFeedback aiFeedback = AiFeedback.builder()
                .journal(journal)
                .content(feedbackContent)
                .build();

        aiFeedbackRepository.save(aiFeedback);

        return journalService.getJournalById(journalId);
    }

    private String generateFeedbackContent(Journal journal) {
        // 실제 AI 로직을 여기에 구현
        // 현재는 더미 데이터로 대체
        StringBuilder feedback = new StringBuilder();
        
        // 손실이 큰 경우
        if (journal.getProfitRate() < -5) {
            feedback.append("큰 손실이 발생한 거래입니다. 매매 전략과 진입 시점을 재검토해보세요. ");
        } 
        // 수익이 좋은 경우
        else if (journal.getProfitRate() > 5) {
            feedback.append("좋은 수익률을 기록했습니다. 성공 요인을 분석해보세요. ");
        }
        
        // 감정 분석
        if (journal.getEmotion() != null) {
            String emotion = journal.getEmotion().toLowerCase();
            if (emotion.contains("불안") || emotion.contains("두려움") || emotion.contains("공포")) {
                feedback.append("감정적인 투자 결정이 있었던 것으로 보입니다. 객관적인 판단을 위해 감정을 배제하려는 노력이 필요합니다. ");
            }
        }
        
        // 전략 분석
        if (journal.getStrategyDescription() != null && journal.getStrategyDescription().length() < 30) {
            feedback.append("매매 전략에 대한 더 자세한 설명을 기록하면 향후 분석에 도움이 됩니다. ");
        }
        
        // 기본 피드백 추가
        feedback.append("다음 거래에서는 진입 전 명확한 목표 수익률과 손절 기준을 설정하는 것이 중요합니다.");
        
        return feedback.toString();
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
} 