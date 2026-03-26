package com.admin.crm.service.impl;

import com.admin.crm.entity.CrmTag;
import com.admin.crm.mapper.CrmTagMapper;
import com.admin.crm.service.CrmTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CrmTagServiceImpl extends ServiceImpl<CrmTagMapper, CrmTag> implements CrmTagService {

    @Override
    public List<CrmTag> getAllTags() {
        QueryWrapper<CrmTag> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
               .orderByAsc("sort");
        return list(wrapper);
    }

    @Override
    public List<CrmTag> getTagsByCategory(String category) {
        QueryWrapper<CrmTag> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.hasText(category), "category", category)
               .eq("status", 1)
               .orderByAsc("sort");
        return list(wrapper);
    }
}
