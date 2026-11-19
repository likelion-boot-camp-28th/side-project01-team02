# side-project01-team02

콘솔 기반 영화 예매 시스템. 요구사항은 [REQUIREMENTS.md](REQUIREMENTS.md) 참고.

## 개발 환경

- Java 21 (팀원 전원 JDK 21 사용)
- Gradle Wrapper 사용 (Gradle 별도 설치 불필요)

## 실행

```bash
./gradlew build   # 컴파일
./gradlew run     # 콘솔 실행
```

Windows에서는 `gradlew.bat`을 사용한다. IntelliJ에서는 `build.gradle`을 Gradle 프로젝트로 연다.

## 패키지 구조

```
com.team02.cinema
├── Application        # 진입점
├── common             # OutputView, InputView, ErrorMessage, BusinessException, BusinessRule (A)
├── member             # 회원 (A)
├── movie              # 영화·상영관·스케줄 (B)
├── reservation        # 예매·좌석 (C)
└── payment            # 결제·할인·환불 (D)
```

각 도메인 패키지 안은 View → Controller → Service → Repository → Domain 계층으로 나눈다.

## 협업 규칙

- `main` 직접 푸시 금지. 기능별 브랜치(`feature/RSV-01`)에서 작업 후 PR
- PR은 1명 이상 승인 후 병합
- 커밋 메시지에 요구사항 ID 포함: `feat: RSV-02 좌석 선택 구현`
- 에러 문구는 `ErrorMessage`, 규칙 숫자는 `BusinessRule`에 모은다
