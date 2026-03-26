package com.admin.system.service.impl;

import com.admin.system.entity.SysRole;
import com.admin.system.mapper.SysRoleMapper;
import com.admin.system.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
