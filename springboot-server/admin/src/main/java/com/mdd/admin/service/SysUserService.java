package com.mdd.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.mdd.admin.config.mybatisplus.mybatisplus.service.BaseServerIService;
import com.mdd.admin.model.SysUserEntity;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public interface SysUserService extends BaseServerIService<SysUserEntity> {

    /**
     * 分页查询
     * @param username 用户名称
     * @param loginName 登录名称
     * @param phone 电话号码
     * @return
     */
    Page<SysUserEntity> selectPage(String username, String loginName, String phone, Integer pageSize, Integer pageNum);
}