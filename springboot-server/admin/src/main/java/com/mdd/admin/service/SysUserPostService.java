package com.mdd.admin.service;


import com.mdd.admin.config.mybatisplus.mybatisplus.service.BaseServerIService;
import com.mdd.admin.model.SysUserPostEntity;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
public interface SysUserPostService extends BaseServerIService<SysUserPostEntity> {

    /**
     * 保存职位用户
     * @param postId
     * @param userIds
     * @return
     */
    boolean saveUser(Long postId, Long[] userIds);

    /**
     * 保存职位
     * @param userId
     * @param postIds
     * @return
     */
    boolean savePost(Long userId, Long[] postIds);

}