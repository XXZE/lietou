package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("crm_recommendation")
public class CrmRecommendation implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long jobId;

    private Long candidateId;

    private Long consultantUserId;

    private String status;

    private Date interviewTime;

    private Integer offerSalary;

    private Date offerDate;

    private Date onboardDate;

    private Date guaranteeStartDate;

    private Date guaranteeEndDate;

    private BigDecimal commission;

    private Integer commissionStatus;

    private String rejectReason;

    private String remark;

    private Date createTime;

    private Date updateTime;
}
