package com.admin.system.service.impl;

import com.admin.system.entity.SysMenu;
import com.admin.system.mapper.SysMenuMapper;
import com.admin.system.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> getMenuTree() {
        List<SysMenu> allMenus = list();
        return buildTree(allMenus, 0L);
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return getMenuTree();
    }

    private List<SysMenu> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(buildTree(menus, menu.getId())))
                .collect(Collectors.toList());
    }
}
