package com.mdd.admin.model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysUserPostTable;
import lombok.Data;
/**
 * @desc用户 - 职位 关联表 Entity
 * @author Maduo  
 * @Create 2018/12/01 19:08
 */
@Data
@TableName("sys_user_post")
public class SysUserPostEntity extends BaseEntity {
      /**
       * 用户ID
       */
      @TableField(SysUserPostTable.USER_ID)
      private Long userId;
      /**
       * 职位ID
       */
      @TableField(SysUserPostTable.POST_ID)
      private Long postId;
}