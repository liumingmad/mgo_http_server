# GitHub Actions 部署配置

## 配置步骤

### 1. 阿里云容器镜像服务配置

1. 登录[阿里云容器镜像服务控制台](https://cr.console.aliyun.com/)
2. 创建命名空间（如果还没有的话）
3. 记录下你的命名空间名称

### 2. 配置 GitHub Secrets

在你的 GitHub 仓库中，前往 Settings > Secrets and variables > Actions，添加以下密钥：

- `ALIYUN_REGISTRY_USERNAME`: 阿里云容器镜像服务的用户名
- `ALIYUN_REGISTRY_PASSWORD`: 阿里云容器镜像服务的密码

### 3. 修改工作流配置

编辑 `.github/workflows/build-and-deploy.yml` 文件：

1. 将 `NAMESPACE: your-namespace` 中的 `your-namespace` 替换为你的实际命名空间
2. 如果需要，可以修改 `REGISTRY` 为你所在地区的镜像仓库地址：
   - 华东1（杭州）: `registry.cn-hangzhou.aliyuncs.com`
   - 华北2（北京）: `registry.cn-beijing.aliyuncs.com`
   - 华东2（上海）: `registry.cn-shanghai.aliyuncs.com`
   - 华南1（深圳）: `registry.cn-shenzhen.aliyuncs.com`

### 4. 工作流触发条件

当前配置在以下情况下会触发构建：
- 推送到 `main` 或 `develop` 分支
- 创建针对 `main` 分支的 Pull Request

### 5. 构建产物

成功构建后，Docker 镜像将会推送到阿里云容器镜像仓库，标签格式为：
- `latest` (仅限 main 分支)
- `分支名-commit哈希前7位`
- 分支名或 PR 编号

### 6. 多架构支持

当前配置支持构建 `linux/amd64` 和 `linux/arm64` 两种架构的镜像。

## 注意事项

1. 确保你的 Spring Boot 应用包含了健康检查端点 (`/actuator/health`)，如果没有，请在 `build.gradle` 中添加：
   ```gradle
   implementation 'org.springframework.boot:spring-boot-starter-actuator'
   ```

2. 如果不需要健康检查，可以从 Dockerfile 中删除 `HEALTHCHECK` 指令。