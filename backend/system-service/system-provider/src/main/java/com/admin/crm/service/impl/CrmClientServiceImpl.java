package com.admin.crm.service.impl;

import com.admin.crm.entity.CrmClient;
import com.admin.crm.mapper.CrmClientMapper;
import com.admin.crm.service.CrmClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CrmClientServiceImpl extends ServiceImpl<CrmClientMapper, CrmClient> implements CrmClientService {

    @Override
    public IPage<CrmClient> selectPage(Page<CrmClient> page, CrmClient client) {
        QueryWrapper<CrmClient> wrapper = new QueryWrapper<>();
        if (client != null) {
            wrapper.like(StringUtils.hasText(client.getName()), "name", client.getName())
                   .eq(StringUtils.hasText(client.getIndustry()), "industry", client.getIndustry())
                   .eq(client.getStatus() != null, "status", client.getStatus())
                   .eq(client.getOwnerUserId() != null, "owner_user_id", client.getOwnerUserId())
                   .eq(client.getIsActive() != null, "is_active", client.getIsActive());
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public CrmClient getClientDetail(Long id) {
        return getById(id);
    }
}
