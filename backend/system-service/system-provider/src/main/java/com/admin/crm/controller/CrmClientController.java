package com.admin.crm.controller;

import com.admin.common.Result;
import com.admin.crm.entity.CrmClient;
import com.admin.crm.service.CrmClientService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crm/client")
public class CrmClientController {

    @Autowired
    private CrmClientService clientService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long ownerUserId) {

        Page<CrmClient> page = new Page<>(current, size);
        CrmClient client = new CrmClient();
        client.setName(name);
        client.setIndustry(industry);
        client.setStatus(status);
        client.setOwnerUserId(ownerUserId);

        IPage<CrmClient> result = clientService.selectPage(page, client);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pages", result.getPages());
        data.put("current", result.getCurrent());
        data.put("size", result.getSize());

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<CrmClient> getById(@PathVariable Long id) {
        CrmClient client = clientService.getClientDetail(id);
        return Result.success(client);
    }

    @PostMapping
    public Result<CrmClient> add(@RequestBody CrmClient client) {
        boolean result = clientService.save(client);
        return result ? Result.success("添加成功", client) : Result.error("添加失败");
    }

    @PutMapping
    public Result<CrmClient> update(@RequestBody CrmClient client) {
        boolean result = clientService.updateById(client);
        return result ? Result.success("更新成功", client) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean result = clientService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }
}
