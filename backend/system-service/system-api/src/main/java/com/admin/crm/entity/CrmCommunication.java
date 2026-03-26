package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_communication")
public class CrmCommunication implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long candidateId;

    private Long clientId;

    private Long jobId;

    private Long recommendationId;

    private String type;

    private String content;

    private Date nextContactTime;

    private Integer isReminded;

    private String attachments;

    private Long createUserId;

    private Date createTime;

    private Date updateTime;
}
