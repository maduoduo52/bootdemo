package com.mdd.admin.service;


import com.mdd.admin.config.mybatisplus.mybatisplus.service.BaseServerIService;
import com.mdd.admin.model.SysPostEntity;

import java.util.List;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public interface SysPostService extends BaseServerIService<SysPostEntity> {

    List<SysPostEntity> selectByLoginName(String loginName);
}