package com.mdd.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.IdWorkerUtil;
import com.mdd.admin.config.mybatisplus.mybatisplus.service.impl.BaseServerServiceImpl;
import com.mdd.admin.dao.SysUserPostDao;
import com.mdd.admin.model.SysUserPostEntity;
import com.mdd.admin.service.SysUserPostService;
import com.mdd.admin.table.SysUserPostTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserPostServiceImpl extends BaseServerServiceImpl<SysUserPostDao, SysUserPostEntity> implements SysUserPostService {

    @Override
    public boolean saveUser(Long postId, Long[] userIds) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysUserPostTable.POST_ID, postId);
        physicsDelete(entityWrapper);
        if(userIds!=null && userIds.length>0){
            for (Long userId : userIds) {
                SysUserPostEntity sys = new SysUserPostEntity();
                sys.setId(IdWorkerUtil.getId());
                sys.setPostId(postId);
                sys.setUserId(userId);
                insert(sys);
            }
        }
        return true;
    }

    @Override
    public boolean savePost(Long userId, Long[] postIds) {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysUserPostTable.USER_ID, userId);
        physicsDelete(entityWrapper);
        if(postIds!=null && postIds.length>0){
            for (Long postId : postIds) {
                SysUserPostEntity sys = new SysUserPostEntity();
                sys.setId(IdWorkerUtil.getId());
                sys.setPostId(postId);
                sys.setUserId(userId);
                insert(sys);
            }
        }
        return true;
    }
}