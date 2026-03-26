package com.admin.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("crm_tag")
public class CrmTag implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String color;

    private String category;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
