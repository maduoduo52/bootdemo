package com.mdd.admin.service.impl;

import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysPostDao;
import com.mdd.admin.model.SysPostEntity;
import com.mdd.admin.service.SysPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPostServiceImpl extends BaseServerServiceImpl<SysPostDao, SysPostEntity> implements SysPostService {

    @Override
    public List<SysPostEntity> selectByLoginName(String loginName) {
        return baseMapper.selectByLoginName(loginName);
    }
}