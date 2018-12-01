package com.mdd.admin.config.mybatisplus.mybatisplus.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.mdd.admin.config.Constant;
import com.mdd.admin.config.IdWorkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @Desc: mybatisPlus 公共字段注入
 * @Author Maduo
 * @Create 2018/12/1 15:16
 */
@Slf4j
public class EwMetaObjectHandler extends MetaObjectHandler {


    /**
     * 新增公共字段注入
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            //设置id
            Object id = getFieldValByName("id", metaObject);
            if (id == null) {
                setFieldValByName("id", IdWorkerUtil.getIdString(), metaObject);
            }
            //设置新增时间
            Object addTime = getFieldValByName("addTime", metaObject);
            if (addTime == null) {
                setFieldValByName("addTime", new Date(), metaObject);
            }
            //设置版本号
            Object version = getFieldValByName("version", metaObject);
            if (version == null) {
                setFieldValByName("version", 0, metaObject);
            }
            //设置逻辑删除标识
            Object deleteFlag = getFieldValByName("deleteFlag", metaObject);
            if (deleteFlag == null) {
                setFieldValByName("deleteFlag", false, metaObject);
            }
            //设置添加人id，（id是否传入，如果没有传入，通过本地线程变量获取）
            Object addEmpId = getFieldValByName("addEmpId", metaObject);
            if (addEmpId == null && Constant.HERDER.get() != null) {
                setFieldValByName("addEmpId", Constant.HERDER.get().getEmpId(), metaObject);
            }
        } catch (Exception e) {
            log.error("添加时公共字段注入异常！！，异常信息：{}", e);
        }
    }

    /**
     * 修改公共字段注入
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            //设置修改时间
            Object updateTime = getFieldValByName("updateTime", metaObject);
            if (updateTime == null) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }
            //设置修改人字段
            Object updateUid = getFieldValByName("updateEmpId", metaObject);
            if (updateUid == null) {
                setFieldValByName("updateEmpId", Constant.HERDER.get().getEmpId(), metaObject);
            }
        } catch (Exception e) {
            log.error("添加时公共字段注入异常！！，异常信息：{}", e);
        }
    }
}
