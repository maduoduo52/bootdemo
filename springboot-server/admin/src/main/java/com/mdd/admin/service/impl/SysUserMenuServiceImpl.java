package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.IdWorkerUtil;
import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysUserMenuDao;
import com.mdd.admin.model.SysUserMenuEntity;
import com.mdd.admin.service.SysUserMenuService;
import com.mdd.admin.table.SysUserMenuTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserMenuServiceImpl extends BaseServerServiceImpl<SysUserMenuDao, SysUserMenuEntity> implements SysUserMenuService {

    @Override
    public boolean save(Long userId, Long[] menuIds) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysUserMenuTable.USER_ID, userId);
        physicsDelete(entityWrapper);
        if(menuIds!=null){
            for (Long menuId : menuIds) {
                SysUserMenuEntity entity = new SysUserMenuEntity();
                entity.setMenuId(menuId);
                entity.setUserId(userId);
                entity.setId(IdWorkerUtil.getId());
                insert(entity);
            }
        }
        return true;
    }
}