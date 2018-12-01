package com.mdd.admin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mdd.admin.model.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
}