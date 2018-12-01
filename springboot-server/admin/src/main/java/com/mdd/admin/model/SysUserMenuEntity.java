package com.mdd.admin.model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysUserMenuTable;
import lombok.Data;
/**
 * @desc用户 - 菜单 关联表 Entity
 * @author Maduo  
 * @Create 2018/12/01 19:08
 */
@Data
@TableName("sys_user_menu")
public class SysUserMenuEntity extends BaseEntity {
      /**
       * 用户ID
       */
      @TableField(SysUserMenuTable.USER_ID)
      private Long userId;
      /**
       * 菜单ID
       */
      @TableField(SysUserMenuTable.MENU_ID)
      private Long menuId;
}