package com.mdd.admin.config.mybatisplus.mybatisplus.model;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 15:59
 */
public enum MVCEnume {

    /**
     * 添加
     */
    insert,
    /**
     * 批量添加
     */
    insertBatch,
    /**
     * 根据id修改不为空的字段
     */
    updateById,
    /**
     * 批量修改
     */
    updateBatchById,
    /**
     * 删除
     */
    delete,
    /**
     * 批量删除
     */
    deleteBatchIds,
    /**
     * 根据ID查询
     */
    selectById,
    /**
     * 查询全部
     */
    selectAll,
    /**
     * 查询全部 以map形式返回
     */
    selectAllMap,
    /**
     * 统计数量
     */
    selectCount,
    /**
     * 根据id集合批量查询
     */
    selectBatchIds,
    /**
     * 根据 对象查询  null字段不会查询
     */
    selectByDtoNotNull,
    /**
     * 根据 对象查询  null字段不会查询
     */
    selectListByDtoNotNull,
}
