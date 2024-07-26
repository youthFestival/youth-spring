# 베이스 이미지 설정 - Ubuntu 최신 버전
FROM ubuntu:latest

# 메타데이터 설정
LABEL authors="seo0j"

# 필수 패키지 설치 및 JDK 17 설치
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget && \
    apt-get clean;

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 필요한 파일들을 복사
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

# Gradle 빌드
RUN ./gradlew build --no-daemon

# 빌드 결과물 복사
COPY build/libs/*.jar app.jar

# 포트 설정
EXPOSE 5000

# 환경 변수 설정
ENV SERVER_PORT=5000

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
