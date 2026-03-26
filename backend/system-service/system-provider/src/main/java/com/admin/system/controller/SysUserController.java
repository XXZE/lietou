package com.admin.system.controller;

import com.admin.system.entity.SysUser;
import com.admin.system.service.SysUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/user")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String status) {
        
        Map<String, Object> result = new HashMap<>();
        Page<SysUser> page = new Page<>(current, size);
        IPage<SysUser> pageResult = sysUserService.selectPage(page, username, status);
        
        result.put("code", 200);
        result.put("data", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("message", "查询成功");
        return result;
    }

    @PostMapping
    public Map<String, Object> add(@RequestBody SysUser user) {
        Map<String, Object> result = new HashMap<>();
        
        SysUser existUser = sysUserService.getByUsername(user.getUsername());
        if (existUser != null) {
            result.put("code", 400);
            result.put("message", "用户名已存在");
            return result;
        }
        
        boolean success = sysUserService.saveUser(user);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody SysUser user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysUserService.updateUser(user);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysUserService.removeById(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = sysUserService.getById(id);
        result.put("code", 200);
        result.put("data", user);
        return result;
    }

    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = new SysUser();
        user.setId(id);
        user.setStatus(status);
        boolean success = sysUserService.updateUser(user);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "状态更新成功" : "状态更新失败");
        return result;
    }

    @PutMapping("/{id}/password")
    public Map<String, Object> resetPassword(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        SysUser user = new SysUser();
        user.setId(id);
        sysUserService.updateUser(user);
        result.put("code", 200);
        result.put("message", "密码重置成功");
        return result;
    }
}
