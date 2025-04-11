# 📘 TradeLog - AI 기반 주식 매매일지 플랫폼

---

## 🧭 서비스 소개

**TradeLog**는 주식 투자자들이 매매일지를 기록하고, AI 분석과 사용자 피드백을 통해 투자 습관을 개선하도록 돕는 플랫폼입니다.

### 핵심 기능
- 매매일지 기록 및 공유
- AI 기반 매매 분석 리포트
- 사용자 간 댓글 피드백 시스템
- JWT 기반 회원 인증
- 전역 예외 처리 및 에러 응답 구조화
- Swagger 기반 API 문서화

---

## 🔧 기술 스택

### 📌 백엔드 (Spring Boot 3)
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- SpringDoc OpenAPI (Swagger 문서)
- Lombok
- Validation

### 💻 프론트엔드 (예정)
- React + Next.js
- TailwindCSS
- Zustand / React Query

---

## 📚 주요 기능

| 기능 | 설명 |
|------|------|
| 회원가입 / 로그인 | JWT 기반 인증 |
| 매매일지 작성 | 종목, 매매일, 수익률, 감정 등 입력 |
| 매매일지 리스트 조회 | 공개된 일지 / 내 일지 구분 |
| 매매일지 상세 조회 | AI 피드백 및 댓글 포함 |
| 댓글 기능 | 타인의 일지에 의견 남기기 |
| AI 분석 피드백 | 반복 손실, 짧은 보유 기간, 고점 매수 등 분석 |
| 예외 처리 | 구조화된 에러 응답 및 전역 예외 처리 |
| API 문서화 | Swagger UI를 통한 API 문서 제공 |

---

## 🔐 API 엔드포인트

### 인증 API
| 메서드 | URL | 설명 |
|--------|-----|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |

### 매매일지 API
| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `/api/journals` | 전체 매매일지 조회 |
| GET | `/api/journals/{id}` | 매매일지 상세 조회 |
| GET | `/api/me/journals` | 내 일지 조회 |
| POST | `/api/journals` | 일지 작성 |
| PUT | `/api/journals/{id}` | 일지 수정 |
| DELETE | `/api/journals/{id}` | 일지 삭제 |

### 댓글 API
| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `/api/journals/{journalId}/comments` | 댓글 목록 조회 |
| POST | `/api/journals/{journalId}/comments` | 댓글 작성 |
| PUT | `/api/journals/{journalId}/comments/{commentId}` | 댓글 수정 |
| DELETE | `/api/journals/{journalId}/comments/{commentId}` | 댓글 삭제 |

### AI 피드백 API
| 메서드 | URL | 설명 |
|--------|-----|------|
| GET | `/api/journals/{journalId}/ai-feedback` | AI 피드백 조회 |
| POST | `/api/journals/{journalId}/ai-feedback` | AI 피드백 자동 생성 |
| POST | `/api/journals/{journalId}/ai-feedback/manual` | AI 피드백 수동 생성/수정 |

---

## 🗂️ ERD (초안)

```plaintext
[User]
- id (PK)
- email
- password
- nickname
- role (USER/ADMIN)
- created_at, updated_at

[Journal]
- id (PK)
- user_id (FK)
- stock_symbol
- buy_date
- sell_date
- profit_rate
- strategy_description
- emotion
- is_public
- created_at, updated_at

[Comment]
- id (PK)
- journal_id (FK)
- user_id (FK)
- content
- created_at

[AiFeedback]
- id (PK)
- journal_id (FK)
- content
- strength_points
- weakness_points
- improvement_suggestions
- created_at, updated_at
```

---

## 🔒 보안 및 인증

- JWT 기반 인증 시스템
- 비밀번호 암호화 (BCrypt)
- API 엔드포인트 접근 제어
- 세션리스 인증 방식

## 📝 API 문서

- Swagger UI: `/api/swagger-ui.html`
- OpenAPI 문서: `/api/api-docs`
- JWT 인증 필요 (로그인 후 토큰 입력)

## ⚠️ 에러 처리

- 구조화된 에러 응답
- 전역 예외 처리
- 유효성 검사 에러 처리
- 리소스 접근 권한 검증
