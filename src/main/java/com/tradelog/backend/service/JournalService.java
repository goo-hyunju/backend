package com.tradelog.backend.service;

import com.tradelog.backend.dto.JournalDto;
import com.tradelog.backend.entity.Journal;
import com.tradelog.backend.entity.User;
import com.tradelog.backend.exception.CustomExceptions.ResourceNotFoundException;
import com.tradelog.backend.exception.CustomExceptions.UnauthorizedAccessException;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Journal> findAllPublicJournals() {
        return journalRepository.findByIsPublicTrue();
    }

    @Transactional(readOnly = true)
    public List<Journal> findJournalsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return journalRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Journal findById(Long id) {
        return journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));
    }

    @Transactional
    public Journal createJournal(String email, JournalDto.CreateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Journal journal = new Journal();
        journal.setUser(user);
        journal.setStockSymbol(request.getStockSymbol());
        journal.setBuyDate(request.getBuyDate());
        journal.setSellDate(request.getSellDate());
        journal.setProfitRate(request.getProfitRate());
        journal.setStrategyDescription(request.getStrategyDescription());
        journal.setEmotion(request.getEmotion());
        journal.setIsPublic(request.getIsPublic());

        return journalRepository.save(journal);
    }

    @Transactional
    public Journal updateJournal(Long id, String email, JournalDto.UpdateRequest request) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!journal.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("매매일지 수정 권한이 없습니다.");
        }

        journal.setStockSymbol(request.getStockSymbol());
        journal.setBuyDate(request.getBuyDate());
        journal.setSellDate(request.getSellDate());
        journal.setProfitRate(request.getProfitRate());
        journal.setStrategyDescription(request.getStrategyDescription());
        journal.setEmotion(request.getEmotion());
        journal.setIsPublic(request.getIsPublic());

        return journalRepository.save(journal);
    }

    @Transactional
    public void deleteJournal(Long id, String email) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", id));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!journal.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("매매일지 삭제 권한이 없습니다.");
        }

        journalRepository.delete(journal);
    }
} 