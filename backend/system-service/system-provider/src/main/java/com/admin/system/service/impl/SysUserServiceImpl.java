package com.admin.system.service.impl;

import com.admin.system.entity.SysUser;
import com.admin.system.mapper.SysUserMapper;
import com.admin.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public Page<SysUser> selectPage(Page<SysUser> page, String username, String status) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(username)) {
            wrapper.like("username", username);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean saveUser(SysUser user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("123456"));
        return save(user);
    }

    @Override
    public boolean updateUser(SysUser user) {
        user.setUpdateTime(new Date());
        return updateById(user);
    }

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }
}
