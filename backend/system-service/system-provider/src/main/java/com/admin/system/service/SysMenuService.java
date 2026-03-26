package com.admin.system.service;

import com.admin.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    
    List<SysMenu> getMenuTree();
    
    List<SysMenu> getMenusByUserId(Long userId);
}
