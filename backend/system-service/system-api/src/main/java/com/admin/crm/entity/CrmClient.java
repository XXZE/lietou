package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_client")
public class CrmClient implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String industry;

    private String financingStage;

    private String scale;

    private String address;

    private String website;

    private String description;

    private String logo;

    private String source;

    private Long ownerUserId;

    private Integer status;

    private String level;

    private Date lastContactTime;

    private Date nextFollowTime;

    private Integer isActive;

    private Date createTime;

    private Date updateTime;

    private Long createUserId;
}
