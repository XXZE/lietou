package com.admin.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String code;
    
    private String name;
    
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
}
