package com.admin.crm.controller;

import com.admin.crm.entity.CrmTag;
import com.admin.crm.service.CrmTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/crm/tag")
public class CrmTagController {

    @Autowired
    private CrmTagService tagService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String category) {
        List<CrmTag> tags;
        if (category != null && !category.isEmpty()) {
            tags = tagService.getTagsByCategory(category);
        } else {
            tags = tagService.getAllTags();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", tags);
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        CrmTag tag = tagService.getById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", tag);
        return response;
    }

    @PostMapping
    public Map<String, Object> add(@RequestBody CrmTag tag) {
        boolean result = tagService.save(tag);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("data", tag);
        return response;
    }

    @PutMapping
    public Map<String, Object> update(@RequestBody CrmTag tag) {
        boolean result = tagService.updateById(tag);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean result = tagService.removeById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        return response;
    }
}
