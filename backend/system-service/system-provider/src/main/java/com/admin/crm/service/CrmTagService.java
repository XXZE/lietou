package com.admin.crm.service;

import com.admin.crm.entity.CrmTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CrmTagService extends IService<CrmTag> {

    List<CrmTag> getAllTags();

    List<CrmTag> getTagsByCategory(String category);
}
