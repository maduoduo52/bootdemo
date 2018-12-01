package com.mdd.admin.config.mybatisplus.mybatisplus.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Desc: 业务扩展层
 * @Author Maduo
 * @Create 2018/12/1 16:12
 */
public interface BaseServerIService<T> extends IService<T> {

    /**
     * 物理删除
     * @param id 数据库ID
     * @return
     */
    boolean physicsDeleteById(Serializable id);

    /**
     * 根据数据库字段条件物理删除
     * @param columnMap 数据字段 只能判断 = 其余无法判断
     * @return
     */
    boolean physicsDeleteByMap(Map<String, Object> columnMap);

    /**
     * 根据wrapper  数据库物理删除
     * @param wrapper
     * @return
     */
    boolean physicsDelete(Wrapper<T> wrapper);

    /**
     * 批量进行物理删除
     * @param idList id集合 in
     * @return
     */
    boolean physicsDeleteBatchIds(List<? extends Serializable> idList);

    /**
     * 根据wapper查询 返回map  key 为ID  value为T对象
     * @param wrapper
     * @return
     * @
     */
    Map<Serializable,T> selectByWrapper(Wrapper<T> wrapper);


    /**
     * 根据ID集合查询 返回map key 为ID  value为T对象
     * @param idList
     * @return
     * @
     */
    Map<Serializable,T> selectByIdList(List<Serializable> idList);
}
