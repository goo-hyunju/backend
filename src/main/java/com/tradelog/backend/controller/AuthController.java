package com.tradelog.backend.controller;

import com.tradelog.backend.dto.UserDto;
import com.tradelog.backend.entity.User;
import com.tradelog.backend.security.JwtTokenProvider;
import com.tradelog.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "회원가입, 로그인 관련 API")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<UserDto.TokenResponse> signup(@Valid @RequestBody UserDto.SignupRequest request) {
        User user = userService.signup(request);
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        return ResponseEntity.ok(new UserDto.TokenResponse(token, user.getEmail(), user.getNickname()));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "등록된 사용자 계정으로 로그인합니다.")
    public ResponseEntity<UserDto.TokenResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByEmail(request.getEmail());
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        return ResponseEntity.ok(new UserDto.TokenResponse(token, user.getEmail(), user.getNickname()));
    }
} 