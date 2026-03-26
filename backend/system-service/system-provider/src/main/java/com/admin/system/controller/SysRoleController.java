package com.admin.system.controller;

import com.admin.system.entity.SysRole;
import com.admin.system.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<SysRole> roles = sysRoleService.list();
        result.put("code", 200);
        result.put("data", roles);
        result.put("message", "查询成功");
        return result;
    }

    @PostMapping
    public Map<String, Object> add(@RequestBody SysRole role) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysRoleService.save(role);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "新增成功" : "新增失败");
        return result;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody SysRole role) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysRoleService.updateById(role);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = sysRoleService.removeById(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}
