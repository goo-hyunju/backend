package com.tradelog.backend.service;

import com.tradelog.backend.dto.comment.CommentRequest;
import com.tradelog.backend.dto.comment.CommentResponse;
import com.tradelog.backend.exception.ResourceNotFoundException;
import com.tradelog.backend.model.Comment;
import com.tradelog.backend.model.Journal;
import com.tradelog.backend.model.User;
import com.tradelog.backend.repository.CommentRepository;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    public Page<CommentResponse> getJournalComments(Long journalId, Pageable pageable) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        // 비공개 일지인 경우 본인만 볼 수 있음
        if (!journal.getIsPublic()) {
            User currentUser = getCurrentUser();
            if (!journal.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("비공개 일지는 작성자만 볼 수 있습니다.");
            }
        }

        return commentRepository.findByJournalOrderByCreatedAtDesc(journal, pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public CommentResponse createComment(Long journalId, CommentRequest request) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        if (!journal.getIsPublic()) {
            throw new IllegalStateException("비공개 일지에는 댓글을 달 수 없습니다.");
        }

        User currentUser = getCurrentUser();

        Comment comment = Comment.builder()
                .journal(journal)
                .user(currentUser)
                .content(request.getContent())
                .build();

        return convertToResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        User currentUser = getCurrentUser();

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
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

    private CommentResponse convertToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .journalId(comment.getJournal().getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
} 