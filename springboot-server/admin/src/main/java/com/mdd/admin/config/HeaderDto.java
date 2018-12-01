package com.mdd.admin.config;

import com.mdd.admin.model.SysUserEntity;
import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/11/25 14:19
 */
@Data
public class HeaderDto {

    /**
     * 请求的emp 即admin后台用户ID
     */
    private Long empId;

    /**
     * 职位ID
     */
    private Long postId;

    /**
     * 用户信息
     */
    private SysUserEntity sysUserEntity;

    private String token;

    /**
     * 私有化构造方法
     */
    private HeaderDto(){}

    /**
     * 初始化
     * @return
     */
    public static HeaderDto initEmp(Long postId, SysUserEntity sysUserEntity){
        HeaderDto herderDto = new HeaderDto();
        herderDto.setEmpId(sysUserEntity.getId());
        herderDto.setPostId(postId);
        herderDto.setSysUserEntity(sysUserEntity);
        return herderDto;
    }

}
