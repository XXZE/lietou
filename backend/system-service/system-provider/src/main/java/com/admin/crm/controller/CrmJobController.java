package com.admin.crm.controller;

import com.admin.common.Result;
import com.admin.crm.entity.CrmJob;
import com.admin.crm.service.CrmJobService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crm/job")
public class CrmJobController {

    @Autowired
    private CrmJobService jobService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer urgencyLevel,
            @RequestParam(required = false) Long ownerUserId) {

        Page<CrmJob> page = new Page<>(current, size);
        CrmJob job = new CrmJob();
        job.setTitle(title);
        job.setClientId(clientId);
        job.setStatus(status);
        job.setUrgencyLevel(urgencyLevel);
        job.setOwnerUserId(ownerUserId);

        IPage<CrmJob> result = jobService.selectPage(page, job);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pages", result.getPages());
        data.put("current", result.getCurrent());
        data.put("size", result.getSize());

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<CrmJob> getById(@PathVariable Long id) {
        CrmJob job = jobService.getJobDetail(id);
        return Result.success(job);
    }

    @GetMapping("/board")
    public Result<Void> board(@RequestParam(required = false) Long ownerUserId) {
        // TODO: 实现看板功能
        return Result.success();
    }

    @PostMapping
    public Result<CrmJob> add(@RequestBody CrmJob job) {
        boolean result = jobService.save(job);
        return result ? Result.success("添加成功", job) : Result.error("添加失败");
    }

    @PutMapping
    public Result<CrmJob> update(@RequestBody CrmJob job) {
        boolean result = jobService.updateById(job);
        return result ? Result.success("更新成功", job) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean result = jobService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }
}
