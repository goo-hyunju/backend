package com.tradelog.backend.service;

import com.tradelog.backend.dto.CommentDto;
import com.tradelog.backend.entity.Comment;
import com.tradelog.backend.entity.Journal;
import com.tradelog.backend.entity.User;
import com.tradelog.backend.exception.CustomExceptions.ResourceNotFoundException;
import com.tradelog.backend.exception.CustomExceptions.UnauthorizedAccessException;
import com.tradelog.backend.repository.CommentRepository;
import com.tradelog.backend.repository.JournalRepository;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Comment> findByJournal(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));
        return commentRepository.findByJournalOrderByCreatedAtDesc(journal);
    }

    @Transactional
    public Comment createComment(Long journalId, String email, CommentDto.CreateRequest request) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Comment comment = new Comment();
        comment.setJournal(journal);
        comment.setUser(user);
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, String email, CommentDto.UpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("댓글 수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
} 