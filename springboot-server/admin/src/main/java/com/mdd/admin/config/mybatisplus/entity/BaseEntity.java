package com.mdd.admin.config.mybatisplus.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.mdd.admin.config.mybatisplus.table.BaseTable;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础字段
 * @author Maduo
 * @Create 2018/11/25 14:25
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(value = BaseTable.ID,type = IdType.ID_WORKER)
    @TableField(value = BaseTable.ID)
    protected Long id;

    /**
     * 添加时间
     */
    @TableField(value = BaseTable.ADD_TIME,fill = FieldFill.INSERT )
    protected Date addTime;

    /**
     * 修改时间
     */
    @TableField(value = BaseTable.UPDATE_TIME,fill = FieldFill.UPDATE )
    protected Date updateTime;

    /**
     * 版本号
     */
    @TableField(value = BaseTable.VERSION ,fill = FieldFill.INSERT )
    @Version
    protected Integer version;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value =  BaseTable.DELETE_FLAG ,fill = FieldFill.INSERT)
    protected Boolean deleteFlag;

    /**
     * 添加人ID  系统用户
     */
    @TableField(value =  BaseTable.ADD_EMP_ID ,fill = FieldFill.INSERT)
    protected Long addEmpId;

    /**
     * 修改人ID
     */
    @TableField(value = BaseTable.UPDATE_EMP_ID,fill = FieldFill.UPDATE)
    protected Long updateEmpId;

}
