package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.IdWorkerUtil;
import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysPostMenuDao;
import com.mdd.admin.model.SysPostMenuEntity;
import com.mdd.admin.service.SysPostMenuService;
import com.mdd.admin.table.SysPostMenuTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysPostMenuServiceImpl extends BaseServerServiceImpl<SysPostMenuDao, SysPostMenuEntity> implements SysPostMenuService {

    @Override
    public boolean save(Long postId, Long[] menuIds) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysPostMenuTable.POST_ID, postId);
        physicsDelete(entityWrapper);
        if(menuIds!=null){
            for (Long menuId : menuIds) {
                SysPostMenuEntity entity = new SysPostMenuEntity();
                entity.setMenuId(menuId);
                entity.setPostId(postId);
                entity.setId(IdWorkerUtil.getId());
                insert(entity);
            }
        }
        return true;
    }
}