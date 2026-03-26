package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_contact")
public class CrmContact implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long clientId;

    private String name;

    private String position;

    private String department;

    private String phone;

    private String email;

    private String wechat;

    private Integer decisionWeight;

    private String communicationPreference;

    private Integer isPrimary;

    private Integer status;

    private String remark;

    private Date createTime;

    private Date updateTime;
}
