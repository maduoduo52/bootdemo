package com.mdd.admin.service.impl;

import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysOrgDao;
import com.mdd.admin.model.SysOrgEntity;
import com.mdd.admin.service.SysOrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysOrgServiceImpl extends BaseServerServiceImpl<SysOrgDao, SysOrgEntity> implements SysOrgService {
}