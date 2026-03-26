-- 修改候选人表薪资字段类型为BIGINT
USE admin_platform;

-- 修改当前年薪字段类型
ALTER TABLE crm_candidate MODIFY COLUMN current_salary BIGINT COMMENT '当前年薪';

-- 修改期望年薪字段类型
ALTER TABLE crm_candidate MODIFY COLUMN expected_salary BIGINT COMMENT '期望年薪';
