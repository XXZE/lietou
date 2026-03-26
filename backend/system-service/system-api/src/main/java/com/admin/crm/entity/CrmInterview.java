package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_interview")
public class CrmInterview implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recommendationId;

    private Integer interviewRound;

    private Date interviewTime;

    private String interviewType;

    private String interviewLocation;

    private String interviewer;

    private Integer interviewResult;

    private String evaluation;

    private Date nextInterviewTime;

    private Integer reminderSent;

    private Date createTime;

    private Date updateTime;
}
