# === 빌드 스테이지: Gradle로 bootJar 생성 ===
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle 래퍼와 빌드 설정 먼저 복사 (의존성 캐시 레이어 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# 소스 복사 후 실행 가능한 jar 빌드 (테스트는 빌드 단계에서 제외)
COPY src src
RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon

# === 실행 스테이지: JRE만 포함한 경량 이미지 ===
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# 빌드 스테이지에서 생성된 jar만 복사 (-plain.jar 제외)
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
