package com.admin.crm.service.impl;

import com.admin.crm.entity.CrmProcessLog;
import com.admin.crm.entity.CrmRecommendation;
import com.admin.crm.mapper.CrmProcessLogMapper;
import com.admin.crm.mapper.CrmRecommendationMapper;
import com.admin.crm.service.CrmRecommendationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class CrmRecommendationServiceImpl extends ServiceImpl<CrmRecommendationMapper, CrmRecommendation> implements CrmRecommendationService {

    private final CrmProcessLogMapper processLogMapper;

    public CrmRecommendationServiceImpl(CrmProcessLogMapper processLogMapper) {
        this.processLogMapper = processLogMapper;
    }

    @Override
    public IPage<CrmRecommendation> selectPage(Page<CrmRecommendation> page, CrmRecommendation recommendation) {
        QueryWrapper<CrmRecommendation> wrapper = new QueryWrapper<>();
        if (recommendation != null) {
            wrapper.eq(recommendation.getJobId() != null, "job_id", recommendation.getJobId())
                   .eq(recommendation.getCandidateId() != null, "candidate_id", recommendation.getCandidateId())
                   .eq(recommendation.getConsultantUserId() != null, "consultant_user_id", recommendation.getConsultantUserId())
                   .eq(StringUtils.hasText(recommendation.getStatus()), "status", recommendation.getStatus());
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public CrmRecommendation getRecommendationDetail(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, String status, Long userId) {
        CrmRecommendation recommendation = getById(id);
        if (recommendation == null) {
            return false;
        }

        String fromStatus = recommendation.getStatus();
        recommendation.setStatus(status);
        boolean result = updateById(recommendation);

        if (result) {
            CrmProcessLog log = new CrmProcessLog();
            log.setRecommendationId(id);
            log.setFromStatus(fromStatus);
            log.setToStatus(status);
            log.setOperatorUserId(userId);
            log.setOperateType("STATUS_CHANGE");
            log.setCreateTime(new java.util.Date());
            processLogMapper.insert(log);
        }

        return result;
    }

    @Override
    public Map<String, Object> getStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        QueryWrapper<CrmRecommendation> wrapper = new QueryWrapper<>();
        wrapper.eq("consultant_user_id", userId);

        // 总推荐数
        long total = count(wrapper);

        // 简历筛选中
        wrapper = new QueryWrapper<>();
        wrapper.eq("consultant_user_id", userId)
               .eq("status", "SCREENING");
        long screening = count(wrapper);

        // 面试中
        wrapper = new QueryWrapper<>();
        wrapper.eq("consultant_user_id", userId)
               .eq("status", "INTERVIEW");
        long interviewing = count(wrapper);

        // Offer中
        wrapper = new QueryWrapper<>();
        wrapper.eq("consultant_user_id", userId)
               .eq("status", "OFFER");
        long offer = count(wrapper);

        // 已入职
        wrapper = new QueryWrapper<>();
        wrapper.eq("consultant_user_id", userId)
               .eq("status", "ONBOARDED");
        long onboarded = count(wrapper);

        stats.put("total", total);
        stats.put("screening", screening);
        stats.put("interviewing", interviewing);
        stats.put("offer", offer);
        stats.put("onboarded", onboarded);

        return stats;
    }
}
