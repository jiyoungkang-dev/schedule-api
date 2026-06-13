# CLAUDE.md

## 프로젝트 개요
아티스트 콘텐츠 일정 관리 API. 아티스트(예: aespa)의 콘텐츠 일정(음악방송, 콘서트, 팬미팅 등)을 등록/조회/관리한다.
핵심 학습 목표는 **JPA 연관관계** 설계와 운영이다.

## 기술 스택
- Java 21
- Spring Boot 4.0.6 (web-mvc, data-jpa, validation)
- JPA / Hibernate
- MySQL (mysql-connector-j)
- Gradle

> Spring 프로젝트 본체는 `./schedule-api/` 하위에 있다. 빌드/테스트는 그 디렉터리에서 `./gradlew` 로 실행한다.
> 베이스 패키지: `com.example.schedule_api`

## 도메인 모델
- **Artist**: id, name (예: aespa)
- **Category**: id, name (예: 음악방송, 콘서트, 팬미팅)
- **Schedule**: id, title, scheduleDate, location, status, description, artist(N:1), category(N:1)

## 코드 규칙
- **계층 분리**: `controller` → `service` → `repository` → `entity`. 컨트롤러에 비즈니스 로직 금지, 리포지토리는 서비스에서만 호출.
- **DTO 변환**: 엔티티를 컨트롤러 밖(요청/응답)으로 그대로 노출하지 않는다. 요청은 `*Request`, 응답은 `*Response` DTO로 받고 내보낸다.
- **URL 프리픽스**: 모든 엔드포인트는 `/api` 로 시작 (예: `/api/schedules`, `/api/artists`).
- **검증**: 요청 DTO에 `jakarta.validation` 애너테이션 사용, 컨트롤러에서 `@Valid`.

## 연관관계 규칙 (핵심)
- **모든 N:1 연관은 `fetch = FetchType.LAZY`** 로 둔다. `@ManyToOne(fetch = LAZY)`. EAGER 금지.
- **연관 엔티티는 DTO에서 id/name만 노출**한다. 예: `ScheduleResponse` 는 `artist` 전체가 아니라 `artistId`, `artistName`(+ `categoryId`, `categoryName`)만 담는다.
- 응답 DTO를 만들 때 연관 엔티티의 다른 필드/역방향 컬렉션을 끌어와 직렬화하지 않는다 (지연로딩 예외·N+1 방지).
- 목록 조회처럼 연관을 같이 써야 하면 fetch join 등으로 의도적으로 가져온다.

## 실수 방지 규칙
- **민감정보는 환경변수로 분리**. DB URL/계정/비밀번호 등을 `application.properties` 에 하드코딩하지 말고 `${DB_URL}` 형태로 주입. 시크릿을 커밋하지 않는다.
- **없는 id 조회는 404**. 서비스에서 조회 실패 시 명확한 예외를 던지고 404로 응답 (200에 빈 값 금지).
- 잘못된 요청은 400, 권한/인증 문제는 401/403 등 상황에 맞는 상태코드를 쓴다.

## Docker 구성 규칙
- **컨테이너 구성은 `docker-compose`로 app + MySQL 8을 함께 띄운다.** app은 DB에 `db` 서비스명(`jdbc:mysql://db:3306/scheduledb`)으로 접속, 시크릿은 `.env`로 주입(`.env.example` 템플릿 제공, `.env`는 커밋 금지), db는 `healthcheck` 통과 후 app 기동(`depends_on: condition: service_healthy`), 데이터는 named volume에 보존, 호스트 포트 충돌 방지로 MySQL은 호스트 `3307`→컨테이너 `3306` 매핑.

## 커밋 메시지 규칙
- **Conventional Commits 타입(feat, fix, chore, build 등)은 영어, 설명은 한국어.** 예: `build: springdoc-openapi 의존성 추가`

## 작업 방식
- **토큰 절약을 위해 간결하게 답한다.** 불필요한 서론·재확인·장황한 설명 생략, 핵심만.

## 규칙 갱신
- **실수하면 이 문서에 규칙을 추가한다.** 같은 실수를 반복하지 않도록, 발견한 함정/합의된 컨벤션을 해당 섹션에 한 줄로 누적한다.
