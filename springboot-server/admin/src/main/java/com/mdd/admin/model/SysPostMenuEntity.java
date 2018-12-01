package com.mdd.admin.model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysPostMenuTable;
import lombok.Data;
/**
 * @desc职位 - 菜单 关联表 Entity
 * @author Maduo  
 * @Create 2018/12/01 19:08
 */
@Data
@TableName("sys_post_menu")
public class SysPostMenuEntity extends BaseEntity {
      /**
       *
       */
      @TableField(SysPostMenuTable.MENU_ID)
      private Long menuId;
      /**
       *
       */
      @TableField(SysPostMenuTable.POST_ID)
      private Long postId;
}