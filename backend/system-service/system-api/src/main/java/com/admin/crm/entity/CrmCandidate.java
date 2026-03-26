package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_candidate")
public class CrmCandidate implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer gender;

    private Date birthday;

    private String phone;

    private String email;

    private String wechat;

    private String currentCompany;

    private String currentPosition;

    private Long currentSalary;

    private Long expectedSalary;

    private String location;

    private Integer workYears;

    private String education;

    private String resumeUrl;

    private String resumeContent;

    private String source;

    private Integer status;

    private String blackReason;

    private Integer isPrivate;

    private Long ownerUserId;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Long createUserId;
}
