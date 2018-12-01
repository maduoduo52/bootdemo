package com.mdd.admin.config.mybatisplus.mybatisplus.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mdd.admin.config.mybatisplus.mybatisplus.MpException;
import com.mdd.admin.config.mybatisplus.mybatisplus.annotations.DisableMVC;
import com.mdd.admin.config.mybatisplus.mybatisplus.model.MVCEnume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Desc: 提供了公共方法，只要是用mybatis plus的形式都支持 减少controller方法数量
 * @Author Maduo
 * @Create 2018/12/1 16:01
 */
@Validated
public abstract class BaseController<M extends ServiceImpl, T> {

    @Autowired
    private M iService;

    private Set<MVCEnume> disableMVCSet = null;

    /**
     * 无参构造  获取注解
     */
    public BaseController() {
        //判断类上面有没有DisableMVC注解
        DisableMVC disableMVC = this.getClass().getAnnotation(DisableMVC.class);
        if (disableMVC != null) {
            //获取禁用的地址信息
            MVCEnume[] address = disableMVC.address();
            if (address != null && address.length > 0) {
                disableMVCSet = new HashSet<>();
                for (MVCEnume mvcEnume : address) {
                    //添加进 disableMVCSet
                    disableMVCSet.add(mvcEnume);
                }
            }
        }
    }

    /**
     * 添加
     *
     * @param t
     * @return
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody T t) {
        if (!disableMVCSet.isEmpty() && disableMVCSet.contains(MVCEnume.insert)) {
            throw new MpException("该接口禁止访问！！");
        }
        return iService.insert(t);
    }

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    @PostMapping("insertBatch")
    public boolean insertBatch(@RequestBody List<T> list) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.insertBatch)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.insertBatch(list);
    }

    /**
     * 修改
     *
     * @param t
     * @return
     */
    @PutMapping("update")
    public boolean updateById(@RequestBody T t) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.updateById)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.updateById(t);
    }

    /**
     * 批量修改
     *
     * @param list
     * @return
     */
    @PutMapping("updateBatchById")
    public boolean updateBatchById(@RequestBody List<T> list) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.updateBatchById)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.updateBatchById(list);
    }

    /**
     * 单个删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}/delete")
    public boolean delete(@NotNull @PathVariable("id") String id) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.delete)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("deleteBatchIds")
    public boolean deleteBatchIds(@RequestBody List<String> ids) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.deleteBatchIds)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.deleteBatchIds(ids);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById")
    public Object selectById(@NotNull String id) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectById)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.selectById(id);
    }

    /**
     * 查询全部 以list形式返回
     *
     * @return
     */
    @GetMapping(value = "selectAll")
    public List selectAll() {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectAll)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.selectList(new EntityWrapper());
    }

    /**
     * 查询全部 以map形式返回
     *
     * @return
     */
    @GetMapping("selectAllMap")
    public Map selectAllMap() {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectAllMap)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.selectMap(new EntityWrapper());
    }

    /**
     * 统计数量
     *
     * @return
     */
    @GetMapping("selectCount")
    public int selectCount() {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectCount)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.selectCount(null);
    }

    /**
     * 根据ID批量查询
     *
     * @param ids
     * @return
     */
    @PostMapping("selectBatchIds")
    public List<T> selectBatchIds(@RequestBody List<String> ids) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectBatchIds)) {
            throw new MpException("该接口禁止访问");
        }
        return iService.selectBatchIds(ids);
    }

    /**
     * 根据 对象查询  null字段不会查询
     *
     * @param t
     * @return
     */
    @PostMapping("selectByDtoNotNull")
    public Object selectByDtoNotNull(@RequestBody T t) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectByDtoNotNull)) {
            throw new MpException("该接口禁止访问");
        }
        EntityWrapper<T> ew = new EntityWrapper();
        ew.setEntity(t);
        return iService.selectOne(ew);
    }

    /**
     * 根据 对象查询  null字段不会查询
     *
     * @param t
     * @return
     */
    @PostMapping("selectListByDtoNotNull")
    public List<T> selectListByDtoNotNull(@RequestBody T t) {
        if (disableMVCSet != null && disableMVCSet.contains(MVCEnume.selectListByDtoNotNull)) {
            throw new MpException("该接口禁止访问");
        }
        EntityWrapper<T> ew = new EntityWrapper();
        ew.setEntity(t);
        return iService.selectList(ew);
    }


    /**
     * get方法
     *
     * @return
     */
    public Set<MVCEnume> getDisableMVCSet() {
        return disableMVCSet;
    }

    /**
     * set方法 空实现 不允许修改
     *
     * @param disableMVCSet
     */
    private void setDisableMVCSet(Set<MVCEnume> disableMVCSet) {
    }
}
