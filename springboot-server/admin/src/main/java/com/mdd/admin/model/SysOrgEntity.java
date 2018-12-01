package com.mdd.admin.model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.mdd.admin.config.mybatisplus.entity.BaseEntity;
import com.mdd.admin.table.SysOrgTable;
import lombok.Data;
/**
 * @desc组织机构表 Entity
 * @author Maduo  
 * @Create 2018/12/01 19:08
 */
@Data
@TableName("sys_org")
public class SysOrgEntity extends BaseEntity {
      /**
       * 菜单名称
       */
      @TableField(SysOrgTable.NAME)
      private String name;
      /**
       * company 公司 department 部门
       */
      @TableField(SysOrgTable.TYPE)
      private String type;
      /**
       * 状态  1 启用 0禁用
       */
      @TableField(SysOrgTable.STATUS)
      private Integer status;
      /**
       * 父节点
       */
      @TableField(SysOrgTable.PARENT)
      private Long parent;
      /**
       * 父节点集合
       */
      @TableField(SysOrgTable.PARENTS)
      private String parents;
      /**
       * 是否为根节点
       */
      @TableField(SysOrgTable.ROOT_FLAG)
      private Integer rootFlag;
      /**
       * 身份编码
       */
      @TableField(SysOrgTable.PROVINCE)
      private String province;
      /**
       * 市 编码 
       */
      @TableField(SysOrgTable.CITY)
      private String city;
      /**
       * 区编码
       */
      @TableField(SysOrgTable.AREA)
      private String area;
      /**
       * 地址
       */
      @TableField(SysOrgTable.ADDRESS)
      private String address;
}