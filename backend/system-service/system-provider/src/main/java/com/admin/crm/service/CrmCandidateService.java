package com.admin.crm.service;

import com.admin.crm.entity.CrmCandidate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CrmCandidateService extends IService<CrmCandidate> {

    IPage<CrmCandidate> selectPage(Page<CrmCandidate> page, CrmCandidate candidate);

    CrmCandidate getCandidateDetail(Long id);

    boolean checkDuplicate(String phone, String email, Long excludeId);
}
