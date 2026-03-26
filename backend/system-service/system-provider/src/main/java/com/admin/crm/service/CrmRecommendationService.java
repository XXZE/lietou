package com.admin.crm.service;

import com.admin.crm.entity.CrmRecommendation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface CrmRecommendationService extends IService<CrmRecommendation> {

    IPage<CrmRecommendation> selectPage(Page<CrmRecommendation> page, CrmRecommendation recommendation);

    CrmRecommendation getRecommendationDetail(Long id);

    boolean updateStatus(Long id, String status, Long userId);

    Map<String, Object> getStatistics(Long userId);
}
