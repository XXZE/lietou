-- ============================================
-- 猎头综合业务管理平台数据库脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS admin_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE admin_platform;

-- ============================================
-- 系统基础表（原有）
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机',
    avatar VARCHAR(255) COMMENT '头像',
    dept_id BIGINT COMMENT '部门ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    path VARCHAR(200) COMMENT '路由路径',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(50) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    type TINYINT DEFAULT 1 COMMENT '类型 1目录 2菜单 3按钮',
    permission VARCHAR(100) COMMENT '权限标识',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_menu (role_id, menu_id),
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    name VARCHAR(50) NOT NULL COMMENT '部门名称',
    sort INT DEFAULT 0 COMMENT '排序',
    leader_user_id BIGINT COMMENT '负责人ID',
    phone VARCHAR(20) COMMENT '联系电话',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ============================================
-- 猎头业务表
-- ============================================

-- 客户表 (CRM Client)
CREATE TABLE IF NOT EXISTS crm_client (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '公司名称',
    industry VARCHAR(50) COMMENT '行业',
    financing_stage VARCHAR(50) COMMENT '融资阶段',
    scale VARCHAR(50) COMMENT '规模(人数)',
    address VARCHAR(255) COMMENT '地址',
    website VARCHAR(255) COMMENT '官网',
    description TEXT COMMENT '公司简介',
    logo VARCHAR(255) COMMENT '公司logo',
    source VARCHAR(50) COMMENT '客户来源',
    owner_user_id BIGINT COMMENT '负责顾问ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常 2公海池',
    level VARCHAR(20) COMMENT '客户等级(A/B/C/D)',
    last_contact_time DATETIME COMMENT '最后联系时间',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    is_active TINYINT DEFAULT 1 COMMENT '是否活跃 0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user_id BIGINT COMMENT '创建人ID',
    INDEX idx_name (name),
    INDEX idx_industry (industry),
    INDEX idx_owner_user_id (owner_user_id),
    INDEX idx_status (status),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 联系人表 (CRM Contact - 决策链)
CREATE TABLE IF NOT EXISTS crm_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL COMMENT '客户ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    position VARCHAR(100) COMMENT '职位',
    department VARCHAR(100) COMMENT '部门',
    phone VARCHAR(20) COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    wechat VARCHAR(50) COMMENT '微信',
    decision_weight TINYINT DEFAULT 1 COMMENT '决策权重 1低 2中 3高',
    communication_preference VARCHAR(50) COMMENT '沟通偏好',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主联系人',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_client_id (client_id),
    INDEX idx_phone (phone),
    INDEX idx_is_primary (is_primary)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系人表';

-- 职位表 (CRM Job)
CREATE TABLE IF NOT EXISTS crm_job (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL COMMENT '客户ID',
    title VARCHAR(100) NOT NULL COMMENT '职位名称',
    department VARCHAR(100) COMMENT '部门',
    location VARCHAR(100) COMMENT '工作地点',
    salary_min INT COMMENT '薪资下限(年薪)',
    salary_max INT COMMENT '薪资上限(年薪)',
    urgency_level TINYINT DEFAULT 2 COMMENT '紧急程度 0 P0紧急 1 P1高 2 P2普通',
    job_description TEXT COMMENT '职位JD',
    requirements TEXT COMMENT '硬性要求',
    tags VARCHAR(500) COMMENT '职位标签(逗号分隔)',
    headcount INT DEFAULT 1 COMMENT '招聘人数',
    candidate_count INT DEFAULT 0 COMMENT '已推荐人数',
    interview_count INT DEFAULT 0 COMMENT '面试人数',
    offer_count INT DEFAULT 0 COMMENT 'Offer人数',
    onboard_count INT DEFAULT 0 COMMENT '入职人数',
    status TINYINT DEFAULT 1 COMMENT '状态 1进行中 2暂停 3已关闭 4已关闭(已录用)',
    close_reason VARCHAR(255) COMMENT '关闭原因',
    interviewer VARCHAR(255) COMMENT '面试官信息',
    attachment_urls TEXT COMMENT '附件(JSON数组)',
    owner_user_id BIGINT COMMENT '负责顾问ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user_id BIGINT COMMENT '创建人ID',
    INDEX idx_client_id (client_id),
    INDEX idx_title (title),
    INDEX idx_status (status),
    INDEX idx_owner_user_id (owner_user_id),
    INDEX idx_urgency_level (urgency_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- 候选人表 (CRM Candidate)
CREATE TABLE IF NOT EXISTS crm_candidate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT COMMENT '性别 0女 1男',
    birthday DATE COMMENT '出生日期',
    phone VARCHAR(20) COMMENT '电话',
    email VARCHAR(100) COMMENT '邮箱',
    wechat VARCHAR(50) COMMENT '微信',
    current_company VARCHAR(100) COMMENT '当前公司',
    current_position VARCHAR(100) COMMENT '当前职位',
    current_salary INT COMMENT '当前年薪',
    expected_salary INT COMMENT '期望年薪',
    location VARCHAR(100) COMMENT '所在地',
    work_years INT COMMENT '工作年限',
    education VARCHAR(20) COMMENT '学历',
    resume_url VARCHAR(255) COMMENT '简历URL',
    resume_content TEXT COMMENT '简历解析内容(JSON)',
    source VARCHAR(50) COMMENT '候选人来源',
    status TINYINT DEFAULT 1 COMMENT '状态 1正常 2已入职 3已淘汰 4黑名单',
    black_reason VARCHAR(255) COMMENT '加入黑名单原因',
    is_private TINYINT DEFAULT 1 COMMENT '是否私有 0否 1是',
    owner_user_id BIGINT COMMENT '负责顾问ID',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_user_id BIGINT COMMENT '创建人ID',
    INDEX idx_name (name),
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_current_company (current_company),
    INDEX idx_status (status),
    INDEX idx_owner_user_id (owner_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候选人表';

-- 候选人标签关联表
CREATE TABLE IF NOT EXISTS crm_candidate_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL COMMENT '候选人ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_candidate_tag (candidate_id, tag_id),
    INDEX idx_candidate_id (candidate_id),
    INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候选人标签关联表';

-- 标签表
CREATE TABLE IF NOT EXISTS crm_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '标签名称',
    color VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    category VARCHAR(50) COMMENT '分类',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 推荐记录表 (CRM Recommendation)
CREATE TABLE IF NOT EXISTS crm_recommendation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL COMMENT '职位ID',
    candidate_id BIGINT NOT NULL COMMENT '候选人ID',
    consultant_user_id BIGINT COMMENT '顾问ID',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 PENDING待筛选 SCREENING简历筛选中 INTERVIEW面试中 OFFER Offer谈判中 ONBOARDING入职中 ONBOARDED已入职 PROBATION过保中 REJECTED已淘汰',
    interview_time DATETIME COMMENT '面试时间',
    offer_salary INT COMMENT 'Offer薪资',
    offer_date DATE COMMENT 'Offer日期',
    onboard_date DATE COMMENT '入职日期',
    guarantee_start_date DATE COMMENT '保期开始日期',
    guarantee_end_date DATE COMMENT '保期结束日期',
    commission DECIMAL(10,2) COMMENT '佣金',
    commission_status TINYINT DEFAULT 0 COMMENT '佣金状态 0未结 1已结',
    reject_reason VARCHAR(255) COMMENT '淘汰原因',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_job_id (job_id),
    INDEX idx_candidate_id (candidate_id),
    INDEX idx_consultant_user_id (consultant_user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐记录表';

-- 流程节点记录表
CREATE TABLE IF NOT EXISTS crm_process_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recommendation_id BIGINT NOT NULL COMMENT '推荐记录ID',
    from_status VARCHAR(20) COMMENT '原状态',
    to_status VARCHAR(20) NOT NULL COMMENT '新状态',
    operator_user_id BIGINT COMMENT '操作人ID',
    operate_type VARCHAR(50) COMMENT '操作类型',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_recommendation_id (recommendation_id),
    INDEX idx_operator_user_id (operator_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程节点记录表';

-- 沟通记录表
CREATE TABLE IF NOT EXISTS crm_communication (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT COMMENT '候选人ID',
    client_id BIGINT COMMENT '客户ID',
    job_id BIGINT COMMENT '职位ID',
    recommendation_id BIGINT COMMENT '推荐记录ID',
    type VARCHAR(20) COMMENT '类型(电话/微信/邮件/面试/其他)',
    content TEXT COMMENT '沟通内容',
    next_contact_time DATETIME COMMENT '下次联系时间',
    is_reminded TINYINT DEFAULT 0 COMMENT '是否已提醒',
    attachments VARCHAR(500) COMMENT '附件',
    create_user_id BIGINT COMMENT '创建人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_candidate_id (candidate_id),
    INDEX idx_client_id (client_id),
    INDEX idx_job_id (job_id),
    INDEX idx_recommendation_id (recommendation_id),
    INDEX idx_next_contact_time (next_contact_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='沟通记录表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS crm_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '操作人ID',
    username VARCHAR(50) COMMENT '操作人用户名',
    operation_type VARCHAR(50) COMMENT '操作类型',
    target_type VARCHAR(50) COMMENT '目标类型(CLIENT/JOB/CANDIDATE/RECOMMENDATION)',
    target_id BIGINT COMMENT '目标ID',
    target_name VARCHAR(100) COMMENT '目标名称',
    operation_detail TEXT COMMENT '操作详情',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_target_type (target_type),
    INDEX idx_target_id (target_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 面试安排表
CREATE TABLE IF NOT EXISTS crm_interview (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recommendation_id BIGINT NOT NULL COMMENT '推荐记录ID',
    interview_round INT DEFAULT 1 COMMENT '面试轮次',
    interview_time DATETIME NOT NULL COMMENT '面试时间',
    interview_type VARCHAR(20) COMMENT '面试类型(电话/视频/现场)',
    interview_location VARCHAR(255) COMMENT '面试地点',
    interviewer VARCHAR(100) COMMENT '面试官',
    interview_result TINYINT COMMENT '面试结果 1通过 2待定 3未通过',
    evaluation TEXT COMMENT '面试评价',
    next_interview_time DATETIME COMMENT '下次面试时间',
    reminder_sent TINYINT DEFAULT 0 COMMENT '是否已发送提醒',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_recommendation_id (recommendation_id),
    INDEX idx_interview_time (interview_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试安排表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员用户 (密码: admin123)
INSERT INTO sys_user (username, password, nickname, email, phone, status) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '超级管理员', 'admin@example.com', '13800138000', 1);

-- 插入默认角色
INSERT INTO sys_role (code, name, status) VALUES
('SUPER_ADMIN', '超级管理员', 1),
('ADMIN', '系统管理员', 1),
('TEAM_LEADER', '团队Leader', 1),
('CONSULTANT', '顾问', 1),
('ASSISTANT', '助理', 1);

-- 插入默认菜单
INSERT INTO sys_menu (parent_id, name, path, component, icon, sort, type, permission, status) VALUES
-- 工作台
(0, '工作台', '/dashboard', 'layout/Layout', 'dashboard', 0, 1, NULL, 1),
(1, '首页', '/dashboard/workplace', 'dashboard/workplace', 'dashboard', 1, 2, NULL, 1),

-- 客户管理
(0, '客户管理', '/crm', 'layout/Layout', 'peoples', 1, 1, NULL, 1),
(5, '客户列表', '/crm/client', 'crm/client/index', 'company', 1, 2, 'crm:client:list', 1),
(5, '公海池', '/crm/client/public', 'crm/client/public', 'people', 2, 2, 'crm:client:public', 1),

-- 职位管理
(0, '职位管理', '/job', 'layout/Layout', 'job', 2, 1, NULL, 1),
(8, '职位中心', '/job/list', 'job/index', 'list', 1, 2, 'job:list', 1),
(8, '职位看板', '/job/board', 'job/board', 'board', 2, 2, 'job:board', 1),

-- 人才库
(0, '人才库', '/talent', 'layout/Layout', 'talent', 3, 1, NULL, 1),
(11, '候选人列表', '/talent/candidate', 'talent/candidate/index', 'user', 1, 2, 'talent:candidate:list', 1),
(11, '人才公海', '/talent/candidate/public', 'talent/candidate/public', 'users', 2, 2, 'talent:candidate:public', 1),
(11, '标签管理', '/talent/tag', 'talent/tag/index', 'tag', 3, 2, 'talent:tag:list', 1),

-- 流程管理
(0, '流程管理', '/process', 'layout/Layout', 'process', 4, 1, NULL, 1),
(15, '推荐记录', '/process/recommendation', 'process/recommendation/index', 'recommend', 1, 2, 'process:recommendation:list', 1),
(15, '面试日历', '/process/interview', 'process/interview/index', 'calendar', 2, 2, 'process:interview:list', 1),

-- 报表中心
(0, '报表中心', '/report', 'layout/Layout', 'chart', 5, 1, NULL, 1),
(18, '业绩看板', '/report/performance', 'report/performance/index', 'performance', 1, 2, 'report:performance', 1),
(18, '转化分析', '/report/conversion', 'report/conversion/index', 'conversion', 2, 2, 'report:conversion', 1),
(18, '客户分析', '/report/client', 'report/client/index', 'client', 3, 2, 'report:client', 1),

-- 系统管理
(0, '系统管理', '/system', 'layout/Layout', 'setting', 6, 1, NULL, 1),
(22, '用户管理', '/system/user', 'system/user/index', 'user', 1, 2, 'system:user:list', 1),
(22, '角色管理', '/system/role', 'system/role/index', 'peoples', 2, 2, 'system:role:list', 1),
(22, '菜单管理', '/system/menu', 'system/menu/index', 'menu', 3, 2, 'system:menu:list', 1),
(22, '部门管理', '/system/dept', 'system/dept/index', 'dept', 4, 2, 'system:dept:list', 1);

-- 初始化默认标签
INSERT INTO crm_tag (name, color, category, sort, status) VALUES
('985/211', '#E6A23C', '学历', 1, 1),
('大厂背景', '#409EFF', '背景', 2, 1),
('可快速入职', '#67C23A', '状态', 3, 1),
('离职观望中', '#F56C6C', '状态', 4, 1),
('Java', '#909399', '技术', 5, 1),
('Python', '#909399', '技术', 6, 1),
('Go', '#909399', '技术', 7, 1),
('前端', '#909399', '技术', 8, 1),
('后端', '#909399', '技术', 9, 1),
('架构师', '#909399', '职位', 10, 1),
('技术经理', '#909399', '职位', 11, 1),
('5年经验', '#909399', '经验', 12, 1),
('10年经验', '#909399', '经验', 13, 1);

-- 给admin用户分配角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
