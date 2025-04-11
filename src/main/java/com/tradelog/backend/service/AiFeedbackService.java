package com.tradelog.backend.service;

import com.tradelog.backend.dto.AiFeedbackDto;
import com.tradelog.backend.entity.AiFeedback;
import com.tradelog.backend.entity.Journal;
import com.tradelog.backend.entity.User;
import com.tradelog.backend.exception.CustomExceptions.ResourceNotFoundException;
import com.tradelog.backend.exception.CustomExceptions.UnauthorizedAccessException;
import com.tradelog.backend.repository.AiFeedbackRepository;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiFeedbackService {
    private final AiFeedbackRepository aiFeedbackRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<AiFeedback> findByJournal(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));
        return aiFeedbackRepository.findByJournal(journal);
    }

    @Transactional
    public AiFeedback createOrUpdateAiFeedback(Long journalId, String email, AiFeedbackDto.CreateRequest request) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // 본인 일지만 AI 피드백 생성 가능
        if (!journal.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("AI 피드백 생성 권한이 없습니다.");
        }

        // 이미 존재하는 경우 업데이트
        AiFeedback aiFeedback = aiFeedbackRepository.findByJournal(journal)
                .orElse(new AiFeedback());

        aiFeedback.setJournal(journal);
        aiFeedback.setContent(request.getContent());
        aiFeedback.setStrengthPoints(request.getStrengthPoints());
        aiFeedback.setWeaknessPoints(request.getWeaknessPoints());
        aiFeedback.setImprovementSuggestions(request.getImprovementSuggestions());

        return aiFeedbackRepository.save(aiFeedback);
    }

    // AI 피드백 생성 로직 (실제 AI 통합 시 구현)
    @Transactional
    public AiFeedback generateAiFeedback(Long journalId, String email) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // 본인 일지만 AI 피드백 생성 가능
        if (!journal.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("AI 피드백 생성 권한이 없습니다.");
        }

        // 이미 존재하는 경우 업데이트
        AiFeedback aiFeedback = aiFeedbackRepository.findByJournal(journal)
                .orElse(new AiFeedback());

        // 실제 AI 통합 시 여기서 AI 서비스 호출
        String content = "매매 전략이 체계적으로 수립되어 있습니다. 감정에 휘둘리지 않고 계획대로 매매한 점이 좋습니다.";
        String strengthPoints = "1. 명확한 진입/퇴출 전략\n2. 감정 통제 능력";
        String weaknessPoints = "1. 손절 라인이 다소 넓음\n2. 진입 타이밍이 다소 늦음";
        String improvementSuggestions = "1. 손절 라인을 좀 더 타이트하게 잡으세요\n2. 진입 타이밍을 더 앞당기는 것을 고려해보세요";

        aiFeedback.setJournal(journal);
        aiFeedback.setContent(content);
        aiFeedback.setStrengthPoints(strengthPoints);
        aiFeedback.setWeaknessPoints(weaknessPoints);
        aiFeedback.setImprovementSuggestions(improvementSuggestions);

        return aiFeedbackRepository.save(aiFeedback);
    }
} 