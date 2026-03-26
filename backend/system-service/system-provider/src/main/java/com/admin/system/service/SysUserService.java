package com.admin.system.service;

import com.admin.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUser> {
    
    Page<SysUser> selectPage(Page<SysUser> page, String username, String status);
    
    boolean saveUser(SysUser user);
    
    boolean updateUser(SysUser user);
    
    SysUser getByUsername(String username);
}
