package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_process_log")
public class CrmProcessLog implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recommendationId;

    private String fromStatus;

    private String toStatus;

    private Long operatorUserId;

    private String operateType;

    private String remark;

    private Date createTime;
}
