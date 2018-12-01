package com.mdd.admin.model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysPostTable;
import lombok.Data;
/**
 * @desc职位表 Entity
 * @author Maduo  
 * @Create 2018/12/01 19:08
 */
@Data
@TableName("sys_post")
public class SysPostEntity extends BaseEntity {
      /**
       * 名称
       */
      @TableField(SysPostTable.NAME)
      private String name;
      /**
       * 状态  1 启用 0禁用
       */
      @TableField(SysPostTable.STATUS)
      private Integer status;
      /**
       * 机构ID
       */
      @TableField(SysPostTable.ORG_ID)
      private String orgId;
}