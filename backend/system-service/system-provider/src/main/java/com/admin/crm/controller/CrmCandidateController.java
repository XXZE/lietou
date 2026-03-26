package com.admin.crm.controller;

import com.admin.common.Result;
import com.admin.crm.entity.CrmCandidate;
import com.admin.crm.service.CrmCandidateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crm/candidate")
public class CrmCandidateController {

    @Autowired
    private CrmCandidateService candidateService;

    // 薪资最大值限制(单位:元)
    private static final Long MAX_SALARY = 1000L * 10000; // 1000万

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String currentCompany,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long ownerUserId,
            @RequestParam(required = false) Integer isPrivate) {

        Page<CrmCandidate> page = new Page<>(current, size);
        CrmCandidate candidate = new CrmCandidate();
        candidate.setName(name);
        candidate.setPhone(phone);
        candidate.setCurrentCompany(currentCompany);
        candidate.setStatus(status);
        candidate.setOwnerUserId(ownerUserId);
        candidate.setIsPrivate(isPrivate);

        IPage<CrmCandidate> result = candidateService.selectPage(page, candidate);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pages", result.getPages());
        data.put("current", result.getCurrent());
        data.put("size", result.getSize());

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<CrmCandidate> getById(@PathVariable Long id) {
        CrmCandidate candidate = candidateService.getCandidateDetail(id);
        return Result.success(candidate);
    }

    @PostMapping
    public Result<CrmCandidate> add(@RequestBody CrmCandidate candidate) {
        // 薪资验证
        if (candidate.getCurrentSalary() != null && candidate.getCurrentSalary() > MAX_SALARY) {
            return Result.error("当前年薪不能超过1000万");
        }
        if (candidate.getExpectedSalary() != null && candidate.getExpectedSalary() > MAX_SALARY) {
            return Result.error("期望年薪不能超过1000万");
        }

        // 查重检查
        if (candidateService.checkDuplicate(candidate.getPhone(), candidate.getEmail(), null)) {
            return Result.error("该候选人已存在（电话或邮箱重复）");
        }

        boolean result = candidateService.save(candidate);
        return result ? Result.success("添加成功", candidate) : Result.error("添加失败");
    }

    @PutMapping
    public Result<CrmCandidate> update(@RequestBody CrmCandidate candidate) {
        // 薪资验证
        if (candidate.getCurrentSalary() != null && candidate.getCurrentSalary() > MAX_SALARY) {
            return Result.error("当前年薪不能超过1000万");
        }
        if (candidate.getExpectedSalary() != null && candidate.getExpectedSalary() > MAX_SALARY) {
            return Result.error("期望年薪不能超过1000万");
        }

        // 查重检查（排除自身）
        if (candidateService.checkDuplicate(candidate.getPhone(), candidate.getEmail(), candidate.getId())) {
            return Result.error("该候选人已存在（电话或邮箱重复）");
        }

        boolean result = candidateService.updateById(candidate);
        return result ? Result.success("更新成功", candidate) : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean result = candidateService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    @GetMapping("/checkDuplicate")
    public Result<Boolean> checkDuplicate(
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = candidateService.checkDuplicate(phone, email, excludeId);
        return Result.success(exists);
    }
}
