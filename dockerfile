# 실행하기 전
# ./gradlew build
# 로 build해놓자.

# 1. OpenJDK 기반 이미지를 사용
FROM openjdk:17-jdk-slim

# 2. 애플리케이션 JAR 파일 복사
ARG JAR_FILE=build/libs/studybbit_back-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# 3. 포트 노출
EXPOSE 8080

# 4. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]