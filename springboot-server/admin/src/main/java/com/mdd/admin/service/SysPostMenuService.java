package com.mdd.admin.service;


import com.mdd.admin.config.mybatisplus.mybatisplus.service.BaseServerIService;
import com.mdd.admin.model.SysPostMenuEntity;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public interface SysPostMenuService extends BaseServerIService<SysPostMenuEntity> {
    boolean save(Long postId, Long[] menuIds);
}