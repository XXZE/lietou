package com.admin.crm.service;

import com.admin.crm.entity.CrmJob;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CrmJobService extends IService<CrmJob> {

    IPage<CrmJob> selectPage(Page<CrmJob> page, CrmJob job);

    CrmJob getJobDetail(Long id);
}
