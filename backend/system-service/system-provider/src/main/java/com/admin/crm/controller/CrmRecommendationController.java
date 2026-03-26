package com.admin.crm.controller;

import com.admin.common.Result;
import com.admin.crm.entity.CrmRecommendation;
import com.admin.crm.service.CrmRecommendationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/crm/recommendation")
public class CrmRecommendationController {

    @Autowired
    private CrmRecommendationService recommendationService;

    @GetMapping("/list")
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Long candidateId,
            @RequestParam(required = false) Long consultantUserId,
            @RequestParam(required = false) String status) {

        Page<CrmRecommendation> page = new Page<>(current, size);
        CrmRecommendation recommendation = new CrmRecommendation();
        recommendation.setJobId(jobId);
        recommendation.setCandidateId(candidateId);
        recommendation.setConsultantUserId(consultantUserId);
        recommendation.setStatus(status);

        IPage<CrmRecommendation> result = recommendationService.selectPage(page, recommendation);

        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pages", result.getPages());
        data.put("current", result.getCurrent());
        data.put("size", result.getSize());

        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<CrmRecommendation> getById(@PathVariable Long id) {
        CrmRecommendation recommendation = recommendationService.getRecommendationDetail(id);
        return Result.success(recommendation);
    }

    @PostMapping
    public Result<CrmRecommendation> add(@RequestBody CrmRecommendation recommendation) {
        // 设置默认状态
        if (recommendation.getStatus() == null) {
            recommendation.setStatus("PENDING");
        }

        boolean result = recommendationService.save(recommendation);
        return result ? Result.success("添加成功", recommendation) : Result.error("添加失败");
    }

    @PutMapping
    public Result<CrmRecommendation> update(@RequestBody CrmRecommendation recommendation) {
        boolean result = recommendationService.updateById(recommendation);
        return result ? Result.success("更新成功", recommendation) : Result.error("更新失败");
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Long userId) {
        boolean result = recommendationService.updateStatus(id, status, userId);
        return result ? Result.success("状态更新成功", null) : Result.error("状态更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean result = recommendationService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics(@RequestParam(required = false) Long userId) {
        Map<String, Object> stats = recommendationService.getStatistics(userId);
        return Result.success(stats);
    }
}
