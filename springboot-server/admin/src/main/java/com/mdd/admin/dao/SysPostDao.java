package com.mdd.admin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mdd.admin.model.SysPostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Maduo
 * @Create 2018/12/01 19:08
 */
@Mapper
public interface SysPostDao extends BaseMapper<SysPostEntity> {

    @Select("SELECT * FROM sys_post WHERE id in(SELECT post_id FROM sys_user_post WHERE user_id in(SELECT id FROM sys_user WHERE delete_flag= 0 and  `login_name` = #{loginName}))")
    List<SysPostEntity> selectByLoginName(@Param("loginName") String loginName);

}