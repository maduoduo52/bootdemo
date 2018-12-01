package com.mdd.admin.service;


import com.mdd.admin.config.mybatisplus.mybatisplus.service.BaseServerIService;
import com.mdd.admin.model.SysUserMenuEntity;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public interface SysUserMenuService extends BaseServerIService<SysUserMenuEntity> {

    boolean save(Long userId, Long[] menuIds);
}