package com.admin.crm.service;

import com.admin.crm.entity.CrmClient;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CrmClientService extends IService<CrmClient> {

    IPage<CrmClient> selectPage(Page<CrmClient> page, CrmClient client);

    CrmClient getClientDetail(Long id);
}
