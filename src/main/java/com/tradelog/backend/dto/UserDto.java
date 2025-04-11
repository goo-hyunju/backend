package com.tradelog.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserDto {

    @Getter
    @Setter
    public static class SignupRequest {
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class TokenResponse {
        private String token;
        private String email;
        private String nickname;

        public TokenResponse(String token, String email, String nickname) {
            this.token = token;
            this.email = email;
            this.nickname = nickname;
        }
    }
} 