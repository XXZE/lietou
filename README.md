# Spring Cloud + Vue 2.0 管理平台

## 项目介绍

这是一个基于 Spring Cloud + Vue 2.0 的企业级管理平台。

## 技术栈

### 后端
- Spring Cloud (Hoxton.SR12)
- Spring Boot 2.3.12
- MyBatis-Plus
- JWT
- MySQL

### 前端
- Vue 2.6.14
- Element UI 2.15.13
- Vuex 3.1.3
- Vue Router 3.5.3

## 项目结构

```
├── backend/                    # Spring Cloud 后端
│   ├── eureka-server/         # 服务注册中心 (8761)
│   ├── gateway-server/        # 网关服务 (8080)
│   ├── config-server/         # 配置中心 (8888)
│   └── system-service/       # 系统服务 (8201)
│
└── frontend/admin-web/        # Vue 2.0 前端 (9528)
```

## 快速启动

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+
- MySQL 8.0+
- Redis (可选)

### 2. 初始化数据库

```bash
# 执行 SQL 脚本创建数据库和表
mysql -u root -p < backend/sql/init.sql
```

### 3. 启动后端服务

```bash
cd backend

# 启动 Eureka 注册中心
cd eureka-server
mvn spring-boot:run

# 启动网关服务 (新终端)
cd gateway-server
mvn spring-boot:run

# 启动系统服务 (新终端)
cd system-service/system-provider
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd frontend/admin-web

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

### 5. 访问系统

- 前端地址: http://localhost:9528
- 默认账号: admin
- 默认密码: admin123

## 服务端口

| 服务 | 端口 |
|------|------|
| Eureka Server | 8761 |
| Gateway Server | 8080 |
| System Service | 8201 |
| Vue Frontend | 9528 |

## 功能模块

- 用户管理
- 角色管理
- 菜单管理
- Dashboard

## 注意事项

1. 首次启动需要确保 MySQL 服务已启动
2. 数据库配置在 `backend/system-service/system-provider/src/main/resources/application.yml`
3. 如需 Redis 支持，修改相应配置文件
