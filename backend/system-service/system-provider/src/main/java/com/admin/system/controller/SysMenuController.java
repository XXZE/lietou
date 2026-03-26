package com.admin.system.controller;

import com.admin.system.entity.SysMenu;
import com.admin.system.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<SysMenu> menus = sysMenuService.list();
        result.put("code", 200);
        result.put("data", menus);
        result.put("message", "查询成功");
        return result;
    }

    @GetMapping("/tree")
    public Map<String, Object> tree() {
        Map<String, Object> result = new HashMap<>();
        List<SysMenu> menuTree = sysMenuService.getMenuTree();
        result.put("code", 200);
        result.put("data", menuTree);
        return result;
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserMenus(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<SysMenu> menus = sysMenuService.getMenusByUserId(userId);
        result.put("code", 200);
        result.put("data", menus);
        return result;
    }

    @PostMapping
    public Map<String, Object> add(@RequestBody SysMenu menu) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysMenuService.save(menu);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody SysMenu menu) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysMenuService.updateById(menu);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysMenuService.removeById(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
