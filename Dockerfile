# 多阶段构建 - 构建阶段
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# 复制 Gradle 配置文件
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# 下载依赖
RUN ./gradlew dependencies --no-daemon

# 复制源代码
COPY src/ src/

# 构建应用
RUN ./gradlew bootJar --no-daemon

# 运行阶段
FROM openjdk:21-jdk-slim

WORKDIR /app

# 安装curl用于健康检查
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 创建非root用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 从构建阶段复制jar文件
COPY --from=builder /app/build/libs/*.jar app.jar

# 创建日志目录
RUN mkdir -p logs

# 更改文件和目录所有者
RUN chown -R appuser:appuser app.jar logs

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8002

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8002/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]