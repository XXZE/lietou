package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_job")
public class CrmJob implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long clientId;

    private String title;

    private String department;

    private String location;

    private Integer salaryMin;

    private Integer salaryMax;

    private Integer urgencyLevel;

    private String jobDescription;

    private String requirements;

    private String tags;

    private Integer headcount;

    private Integer candidateCount;

    private Integer interviewCount;

    private Integer offerCount;

    private Integer onboardCount;

    private Integer status;

    private String closeReason;

    private String interviewer;

    private String attachmentUrls;

    private Long ownerUserId;

    private Date createTime;

    private Date updateTime;

    private Long createUserId;
}
