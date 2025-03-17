# 필요 프로그램 설치
FROM openjdk:17-jdk-slim as stage1

# 파일 복사
WORKDIR /app
COPY gradle gradle
COPY src src
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
RUN chmod +x gradlew

# 빌드
RUN ./gradlew clean bootJar

# 두 번째 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar app.jar

# 한국 시간
RUN apt update && apt install -y tzdata
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo "Asia/Seoul" > /etc/timezone

# 실행
ENTRYPOINT [ "java", "-jar", "app.jar" ]