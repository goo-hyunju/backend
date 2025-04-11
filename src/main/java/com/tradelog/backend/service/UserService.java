package com.tradelog.backend.service;

import com.tradelog.backend.dto.UserDto;
import com.tradelog.backend.entity.User;
import com.tradelog.backend.entity.UserRole;
import com.tradelog.backend.exception.CustomExceptions.InvalidRequestException;
import com.tradelog.backend.exception.CustomExceptions.ResourceNotFoundException;
import com.tradelog.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(UserDto.SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new InvalidRequestException("이미 사용 중인 닉네임입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole(UserRole.USER);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
} 