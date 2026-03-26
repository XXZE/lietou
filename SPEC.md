# 猎头综合业务管理平台 - 技术规格说明书

## 1. 项目概述

- **项目名称**: 猎头综合业务管理平台 (Hunter CRM)
- **项目类型**: 企业级SaaS管理后台系统
- **核心功能**: 客户管理、职位管理、候选人管理、流程管理、报表分析
- **目标用户**: 猎头顾问、团队Leader、管理员

## 2. 技术架构

### 2.1 后端技术栈
- **核心框架**: Spring Cloud (Hoxton.SR12)
- **微服务组件**:
  - Spring Cloud Netflix Eureka (服务注册与发现)
  - Spring Cloud Gateway (API网关)
  - Spring Cloud OpenFeign (服务间调用)
  - Spring Cloud Config (配置中心)
- **应用框架**: Spring Boot 2.3.12.RELEASE
- **数据库**: MySQL 8.0 + MyBatis-Plus
- **安全**: Spring Security + JWT

### 2.2 前端技术栈
- **核心框架**: Vue 2.6.14
- **UI组件库**: Element UI 2.15.13
- **状态管理**: Vuex 3.1.3
- **路由**: Vue Router 3.5.3
- **HTTP客户端**: Axios 0.21.4
- **构建工具**: Vue CLI 4.5.19

### 2.3 项目结构

```
├── backend/                    # Spring Cloud 后端
│   ├── eureka-server/         # 服务注册中心 (8761)
│   ├── gateway-server/         # 网关服务 (8080)
│   ├── config-server/          # 配置中心 (8888)
│   └── system-service/        # 系统服务 (8201)
│       ├── system-api/        # API接口定义
│       │   └── crm/entity/     # 业务实体
│       └── system-provider/   # 服务提供者
│           └── crm/
│               ├── mapper/    # 数据访问层
│               ├── service/   # 业务逻辑层
│               └── controller/# 接口层
│
└── frontend/                   # Vue 2.0 前端
    └── admin-web/             # 管理后台前端 (9528)
        └── src/
            ├── api/           # API接口封装
            ├── router/        # 路由配置
            ├── store/        # Vuex状态管理
            ├── views/        # 页面组件
            │   ├── crm/       # 客户管理模块
            │   ├── job/      # 职位管理模块
            │   ├── talent/   # 人才库模块
            │   ├── process/  # 流程管理模块
            │   ├── report/   # 报表分析模块
            │   └── dashboard/# 工作台
            └── utils/        # 工具函数
```

## 3. 功能模块

### 3.1 工作台 (Dashboard)
- 统计概览：客户数量、进行中职位、候选人库、推荐总数
- 待办任务：待推荐、待反馈、今日面试、待跟进客户
- 快捷操作：新增客户、发布职位、录入候选人、推荐候选人
- 转化漏斗：总推荐→面试→Offer→入职

### 3.2 客户管理 (CRM)
- **客户列表**: 新增/编辑/删除客户、公司信息管理
- **客户档案**: 
  - 基础信息：公司名称、行业、融资阶段、规模、地址
  - 决策链管理：联系人管理、决策权重
  - 合作历史：推荐人数、录用人数统计
  - 活跃度：30天内是否有职位或沟通记录

### 3.3 职位管理 (Job Order)
- **职位列表**: 发布/编辑/删除职位
- **职位信息**: 
  - 基本信息：职位名称、部门、地点、薪资范围
  - 紧急程度：P0/P1/P2
  - 职位JD、硬性要求、职位标签
- **职位看板**: Kanban视图展示状态（进行中/暂停/关闭）

### 3.4 人才库 (Talent Pool)
- **候选人列表**: 新增/编辑/删除候选人
- **候选人信息**:
  - 基本信息：姓名、性别、电话、邮箱
  - 工作信息：公司、职位、年薪、期望薪资
  - 教育背景：学历、工作年限
  - 简历管理：简历URL
- **标签管理**: 自定义标签（学历、背景、状态、技术等）
- **查重机制**: 电话/邮箱重复检测

### 3.5 流程管理 (Pipeline)
- **推荐记录**: 推荐候选人到职位
- **状态流转**: 
  - 待推荐 → 简历筛选 → 面试中 → Offer谈判 → 入职中 → 已入职
  - 已淘汰
- **流程日志**: 记录每次状态变更

### 3.6 报表中心 (Report)
- **业绩看板**: 推荐数、面试数、Offer数、入职数统计
- **转化分析**: 漏斗转化率
- **状态分布**: 各状态候选人数量

### 3.7 系统管理
- 用户管理
- 角色管理（管理员、团队Leader、顾问、助理）
- 菜单管理
- 部门管理

## 4. 数据库设计

### 4.1 核心业务表

| 表名 | 说明 |
|------|------|
| crm_client | 客户表 |
| crm_contact | 联系人表（决策链） |
| crm_job | 职位表 |
| crm_candidate | 候选人表 |
| crm_candidate_tag | 候选人标签关联表 |
| crm_tag | 标签表 |
| crm_recommendation | 推荐记录表 |
| crm_process_log | 流程日志表 |
| crm_communication | 沟通记录表 |
| crm_interview | 面试安排表 |
| crm_operation_log | 操作日志表 |

### 4.2 状态流转

```
PENDING(待推荐) 
  → SCREENING(简历筛选中) 
  → INTERVIEW(面试中) 
  → OFFER(Offer谈判中) 
  → ONBOARDING(入职中) 
  → ONBOARDED(已入职)
  
PENDING → REJECTED(已淘汰)
```

## 5. API 接口设计

### 客户接口
- `GET /crm/client/list` - 客户列表
- `GET /crm/client/{id}` - 客户详情
- `POST /crm/client` - 新增客户
- `PUT /crm/client` - 更新客户
- `DELETE /crm/client/{id}` - 删除客户

### 职位接口
- `GET /crm/job/list` - 职位列表
- `GET /crm/job/{id}` - 职位详情
- `POST /crm/job` - 新增职位
- `PUT /crm/job` - 更新职位
- `DELETE /crm/job/{id}` - 删除职位

### 候选人接口
- `GET /crm/candidate/list` - 候选人列表
- `GET /crm/candidate/{id}` - 候选人详情
- `POST /crm/candidate` - 新增候选人
- `PUT /crm/candidate` - 更新候选人
- `DELETE /crm/candidate/{id}` - 删除候选人
- `GET /crm/candidate/checkDuplicate` - 查重检测

### 推荐记录接口
- `GET /crm/recommendation/list` - 推荐列表
- `GET /crm/recommendation/{id}` - 推荐详情
- `POST /crm/recommendation` - 新增推荐
- `PUT /crm/recommendation` - 更新推荐
- `PUT /crm/recommendation/status/{id}` - 状态流转
- `GET /crm/recommendation/statistics` - 统计数据

### 标签接口
- `GET /crm/tag/list` - 标签列表
- `POST /crm/tag` - 新增标签
- `PUT /crm/tag` - 更新标签
- `DELETE /crm/tag/{id}` - 删除标签

## 6. 页面设计

### 6.1 配色方案
- 主色调: #409EFF (Element UI 蓝)
- 侧边栏: #304156 (深色)
- 背景色: #f0f2f5

### 6.2 布局
- 左侧侧边栏导航
- 顶部header（用户信息、退出）
- 内容区域
- 标签式多页签

## 7. 关键业务逻辑

### 7.1 查重机制
当录入新候选人时，系统自动检索库内是否存在相同电话或邮箱，防止重复推荐。

### 7.2 状态流转日志
每一次状态变更都会记录到流程日志表，包括原状态、新状态、操作人、操作时间、备注。

### 7.3 数据脱敏
敏感数据（候选人电话、客户核心联系人）需脱敏显示（如 138****0000）。

## 8. 验收标准

1. 后端各服务可正常启动
2. 前端项目可正常启动运行
3. 登录功能正常工作
4. 客户管理CRUD功能完整
5. 职位管理CRUD功能完整
6. 候选人管理CRUD功能完整（含查重）
7. 推荐记录及状态流转功能正常
8. 业绩看板数据展示正常
9. 菜单权限正常加载
10. 页面无明显报错

## 9. 服务端口

| 服务 | 端口 |
|------|------|
| Eureka Server | 8761 |
| Gateway Server | 8080 |
| System Service | 8201 |
| Vue Frontend | 9528 |

## 10. 默认账号

- 用户名: admin
- 密码: admin123
