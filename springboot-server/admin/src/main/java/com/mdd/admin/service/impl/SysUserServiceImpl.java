package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysUserDao;
import com.mdd.admin.model.SysUserEntity;
import com.mdd.admin.service.SysUserService;
import com.mdd.admin.table.SysUserPostTable;
import com.mdd.admin.table.SysUserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends BaseServerServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserPostServiceImpl sysUserPostService;

    @Override
    public boolean deleteById(Serializable id) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq(SysUserPostTable.USER_ID,id);
        sysUserPostService.delete(wrapper);
        return super.deleteById(id);
    }

    @Override
    public Page<SysUserEntity> selectPage(String username, String loginName, String phone, Integer pageSize, Integer pageNum) {
        EntityWrapper wrapper = new EntityWrapper();
        if(!StringUtils.isEmpty(username)){
            wrapper.like(SysUserTable.NAME,username);
        }
        if(!StringUtils.isEmpty(username)){
            wrapper.like(SysUserTable.LOGIN_NAME,loginName);
        }
        if(!StringUtils.isEmpty(username)){
            wrapper.like(SysUserTable.PHONE,phone);
        }
        Page<SysUserEntity> page = new Page<>(pageSize, pageNum);
        return selectPage(page,wrapper);
    }
}