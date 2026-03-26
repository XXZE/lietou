package com.admin.crm.service.impl;

import com.admin.crm.entity.CrmCandidate;
import com.admin.crm.mapper.CrmCandidateMapper;
import com.admin.crm.service.CrmCandidateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CrmCandidateServiceImpl extends ServiceImpl<CrmCandidateMapper, CrmCandidate> implements CrmCandidateService {

    @Override
    public IPage<CrmCandidate> selectPage(Page<CrmCandidate> page, CrmCandidate candidate) {
        QueryWrapper<CrmCandidate> wrapper = new QueryWrapper<>();
        if (candidate != null) {
            wrapper.like(StringUtils.hasText(candidate.getName()), "name", candidate.getName())
                   .like(StringUtils.hasText(candidate.getPhone()), "phone", candidate.getPhone())
                   .like(StringUtils.hasText(candidate.getCurrentCompany()), "current_company", candidate.getCurrentCompany())
                   .eq(candidate.getStatus() != null, "status", candidate.getStatus())
                   .eq(candidate.getOwnerUserId() != null, "owner_user_id", candidate.getOwnerUserId())
                   .eq(candidate.getIsPrivate() != null, "is_private", candidate.getIsPrivate());
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public CrmCandidate getCandidateDetail(Long id) {
        return getById(id);
    }

    @Override
    public boolean checkDuplicate(String phone, String email, Long excludeId) {
        QueryWrapper<CrmCandidate> wrapper = new QueryWrapper<>();
        wrapper.and(w -> w
                .eq(StringUtils.hasText(phone), "phone", phone)
                .or()
                .eq(StringUtils.hasText(email), "email", email)
        );
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        return count(wrapper) > 0;
    }
}
