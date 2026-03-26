package com.admin.crm.service.impl;

import com.admin.crm.entity.CrmJob;
import com.admin.crm.mapper.CrmJobMapper;
import com.admin.crm.service.CrmJobService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CrmJobServiceImpl extends ServiceImpl<CrmJobMapper, CrmJob> implements CrmJobService {

    @Override
    public IPage<CrmJob> selectPage(Page<CrmJob> page, CrmJob job) {
        QueryWrapper<CrmJob> wrapper = new QueryWrapper<>();
        if (job != null) {
            wrapper.like(StringUtils.hasText(job.getTitle()), "title", job.getTitle())
                   .eq(job.getClientId() != null, "client_id", job.getClientId())
                   .eq(job.getStatus() != null, "status", job.getStatus())
                   .eq(job.getUrgencyLevel() != null, "urgency_level", job.getUrgencyLevel())
                   .eq(job.getOwnerUserId() != null, "owner_user_id", job.getOwnerUserId());
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public CrmJob getJobDetail(Long id) {
        return getById(id);
    }
}
