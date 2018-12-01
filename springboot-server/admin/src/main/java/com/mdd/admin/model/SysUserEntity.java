package com.mdd.admin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysUserTable;
import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 18:02
 */
@Data
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {

    /**
     * 用户名称
     */
    @TableField(SysUserTable.NAME)
    private String name;
    /**
     * 登录名
     */
    @TableField(SysUserTable.LOGIN_NAME)
    private String loginName;
    /**
     * 盐
     */
    @TableField(SysUserTable.SALT)
    private String salt;
    /**
     * 密码
     */
    @TableField(SysUserTable.PWD)
    private String pwd;
    /**
     * 0 禁用  1启用
     */
    @TableField(SysUserTable.STATUS)
    private Boolean status;
    /**
     * 机构ID
     */
    @TableField(SysUserTable.ORG_ID)
    private String orgId;
    /**
     * 手机号码
     */
    @TableField(SysUserTable.PHONE)
    private String phone;
    /**
     * 身份证号码
     */
    @TableField(SysUserTable.ID_CARD)
    private String idCard;
    /**
     * 性别 1: 男  2：女  3：其他
     */
    @TableField(SysUserTable.SEX)
    private Integer sex;
    /**
     * 是否为超级管理员
     */
    @TableField(SysUserTable.ADMIN)
    private Boolean admin;
}
