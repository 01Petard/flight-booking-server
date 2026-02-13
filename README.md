# ✈️ 图灵航空 - 智能航班预订系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M3-blue.svg)](https://spring.io/projects/spring-ai)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-4fc08d.svg)](https://vuejs.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

## 📖 项目简介

图灵航空智能航班预订系统是一个基于Spring AI的现代化航班预订平台，集成了AI聊天机器人、向量数据库和RAG（检索增强生成）等先进技术。系统提供智能化的客户服务，支持航班查询、预订、修改和取消等核心功能。

### 🎯 项目特色

- 🤖 **AI智能客服**：基于大语言模型的智能对话系统
- 🔍 **RAG检索增强**：结合知识库的智能问答能力
- 💬 **流式对话**：支持实时流式响应的聊天体验
- 📱 **响应式设计**：现代化的Web界面，支持多设备访问
- 🚀 **微服务架构**：基于Spring Boot的模块化设计
- 🔧 **函数调用**：AI驱动的业务操作自动化

## 🏗️ 技术架构

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17+ | 编程语言 |
| Spring Boot | 3.2.5 | 应用框架 |
| Spring AI | 1.0.0-M3 | AI集成框架 |
| Ollama | - | 本地AI模型 |
| OpenAI | - | 云端AI模型 |
| Maven | - | 依赖管理 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue.js | 3.x | 前端框架 |
| Element Plus | - | UI组件库 |
| Axios | - | HTTP客户端 |
| Vite | - | 构建工具 |

### 系统架构图

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端界面      │    │   Spring Boot   │    │   AI模型服务    │
│   (Vue.js)     │◄──►│   后端服务      │◄──►│  (Ollama/      │
│                 │    │                 │    │   OpenAI)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   向量数据库    │
                       │  (RAG检索)     │
                       └─────────────────┘
```

## 🚀 快速开始

### 环境要求

- **Java**: 17 或更高版本
- **Maven**: 3.6+ 
- **Node.js**: 16+ (用于前端开发)
- **Ollama**: 本地AI模型服务 (可选)

### 1. 克隆项目

```bash
git clone https://github.com/your-username/flight-booking.git
cd flight-booking
```

### 2. 后端启动

```bash
# 进入项目根目录
cd flight-booking

# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

### 3. 前端启动

```bash
# 进入前端目录
cd app/flight-booking-web

# 安装依赖
npm install
# 或使用 pnpm
pnpm install

# 启动开发服务器
npm run dev
# 或
pnpm dev
```

前端将在 `http://localhost:5173` 启动

### 4. 配置AI模型

编辑 `src/main/resources/application.yml` 文件：

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: qwen2.5:7b
      embedding:
        model: shaw/dmeta-embedding-zh
```

## 📚 功能特性

### 🔍 航班预订管理

- **预订查询**: 支持按预订号和客户姓名查询
- **预订修改**: 支持修改航班日期、出发地和目的地
- **预订取消**: 支持取消已确认的预订
- **状态管理**: 实时跟踪预订状态变化

### 🤖 AI智能客服

- **自然语言交互**: 支持中文对话
- **上下文理解**: 基于聊天记忆的连续对话
- **智能推荐**: 根据用户需求提供个性化服务
- **业务操作**: 通过函数调用执行预订操作

### 📊 数据管理

- **演示数据**: 自动生成示例客户和预订信息
- **实时更新**: 支持数据的实时同步
- **状态验证**: 业务规则验证和错误处理

## 🔌 API接口文档

### 基础信息

- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

### 航班预订接口

#### 1. 获取预订列表

```http
GET /api/booking/list
```

**响应示例**:
```json
[
  {
    "bookingNumber": "101",
    "name": "徐庶",
    "date": "2024-01-15",
    "bookingStatus": "CONFIRMED",
    "from": "北京",
    "to": "上海",
    "bookingClass": "ECONOMY"
  }
]
```

### AI聊天接口

#### 1. 流式聊天

```http
GET /api/ai/chat/stream?message={用户消息}
```

**特点**:
- 支持Server-Sent Events (SSE)
- 实时流式响应
- 自动添加完成标记 `[complete]`

#### 2. 同步聊天

```http
GET /api/ai/chat?message={用户消息}
```

**特点**:
- 等待完整回复后返回
- 适用于非实时场景

### 函数调用接口

系统支持以下AI函数调用：

#### getBookingDetails
查询航班预订详情
```json
{
  "bookingNumber": "101",
  "name": "徐庶"
}
```

#### changeBooking
修改航班预订信息
```json
{
  "bookingNumber": "101",
  "name": "徐庶",
  "date": "2024-01-20",
  "from": "北京",
  "to": "广州"
}
```

#### cancelBooking
取消航班预订
```json
{
  "bookingNumber": "101",
  "name": "徐庶"
}
```

## 🎨 前端界面

### 主要功能区域

1. **航班预订列表**
   - 显示所有预订信息
   - 支持状态标识和操作按钮
   - 响应式表格设计

2. **AI智能客服**
   - 实时聊天界面
   - 支持流式响应显示
   - 聊天历史记录管理

### 界面特性

- 🎨 现代化设计风格
- 📱 响应式布局，支持多设备
- 🌈 丰富的视觉反馈
- ⚡ 流畅的用户交互

## ⚙️ 配置说明

### 应用配置

主要配置文件：`src/main/resources/application.yml`

```yaml
server:
  port: 8080
  servlet:
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      embedding:
        model: shaw/dmeta-embedding-zh
      chat:
        model: qwen2.5:7b
        options:
          temperature: 0.8
```

### AI模型配置

支持多种AI模型：

1. **Ollama (本地)**
   - 支持离线运行
   - 可自定义模型
   - 适合开发和测试

2. **OpenAI (云端)**
   - 高性能模型
   - 需要API密钥
   - 适合生产环境

3. **其他模型**
   - 支持Spring AI兼容的模型
   - 可扩展配置

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ApiTests

# 生成测试报告
mvn test jacoco:report
```

### 测试覆盖

- **单元测试**: 核心业务逻辑
- **集成测试**: API接口测试
- **端到端测试**: 完整业务流程

## 🚀 部署

### 生产环境部署

1. **打包应用**
```bash
mvn clean package -DskipTests
```

2. **运行JAR文件**
```bash
java -jar target/flight-booking-1.0.jar
```

3. **Docker部署** (可选)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/flight-booking-1.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 环境变量配置

```bash
# AI模型配置
export SPRING_AI_OPENAI_API_KEY=your-api-key
export SPRING_AI_OPENAI_BASE_URL=https://api.openai.com/v1

# 数据库配置
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/flight_booking
export SPRING_DATASOURCE_USERNAME=username
export SPRING_DATASOURCE_PASSWORD=password
```

## 🔧 开发指南

### 项目结构

```
flight-booking/
├── src/main/java/com/hzx/ai/
│   ├── controller/          # 控制器层
│   ├── model/              # 数据模型
│   ├── services/           # 业务服务
│   └── FlightBookingApplication.java  # 主应用类
├── src/main/resources/     # 配置文件
├── app/flight-booking-web/ # 前端应用
└── pom.xml                 # Maven配置
```

### 代码规范

- 使用Java 17+ 特性
- 遵循Spring Boot最佳实践
- 完整的JavaDoc注释
- 统一的代码格式化

### 扩展开发

1. **添加新的AI模型**
   - 实现相应的配置类
   - 更新配置文件
   - 添加测试用例

2. **扩展业务功能**
   - 创建新的服务类
   - 添加控制器接口
   - 更新前端界面

## 🐛 故障排除

### 常见问题

1. **AI模型连接失败**
   - 检查模型服务是否启动
   - 验证配置文件中的URL
   - 确认网络连接正常

2. **前端无法连接后端**
   - 检查后端服务状态
   - 验证API接口路径
   - 确认跨域配置正确

3. **预订操作失败**
   - 检查业务规则验证
   - 查看系统日志
   - 确认数据完整性

### 日志查看

```bash
# 查看应用日志
tail -f logs/application.log

# 查看错误日志
grep ERROR logs/application.log
```

## 📝 更新日志

### v1.0.0 (2024-01-01)
- ✨ 初始版本发布
- 🚀 支持基本的航班预订功能
- 🤖 集成AI智能客服
- 💬 支持流式对话响应
- 🔍 集成RAG检索功能

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

## 👥 团队

- **项目负责人**: xushu
- **开发团队**: 图灵航空技术团队
- **联系方式**: [your-email@example.com]

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 优秀的Java应用框架
- [Spring AI](https://spring.io/projects/spring-ai) - AI集成框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - Vue 3 UI组件库

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！
