package com.mdd.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.Result;
import com.mdd.admin.model.SysOrgEntity;
import com.mdd.admin.service.SysOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 18:22
 */
@Controller
@RequestMapping("org")
public class OrgController {

    @Autowired
    private SysOrgService sysOrgService;

    /**
     * 组织机构界面
     * @return
     * @throws Exception
     */
    @GetMapping("list.html")
    public String list()throws Exception{
        return "org/list";
    }

    /**
     * 获取全部数据
     * @return
     * @throws Exception
     */
    @GetMapping("getAll.html")
    @ResponseBody
    public Result getAll()throws Exception{
        List<SysOrgEntity> list = sysOrgService.selectList(new EntityWrapper<>());
        return Result.success(list,"组织机构数据");
    }

    /**
     * 根据ID查询信息
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("getById.html")
    @ResponseBody
    public Result getById(@NotNull(message = "id不能为空") Long id)throws Exception{
        return Result.success(sysOrgService.selectById(id),"机构信息");
    }

    /**
     * 保存数据
     * @param sysOrgEntity
     * @return
     * @throws Exception
     */
    @PostMapping("save.html")
    @ResponseBody
    public Result save(SysOrgEntity sysOrgEntity)throws Exception{
        boolean flag ;
        if(sysOrgEntity.getId()!=null){
            sysOrgEntity.setParent(null);
            flag = sysOrgService.updateById(sysOrgEntity);
        }else{
            flag = sysOrgService.insert(sysOrgEntity);
        }
        if(flag){
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }

    /**
     * 删除
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("delete.html")
    @ResponseBody
    public Result delete(String id)throws Exception{
        boolean flag  = sysOrgService.deleteById(id);
        if(flag){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 保存界面
     * @return
     * @throws Exception
     */
    @GetMapping("savePage.html")
    public String savePage(Long id, Model model)throws Exception{
        SysOrgEntity entity;
        if(id==null){
            entity = new SysOrgEntity();
        }else{
            entity = sysOrgService.selectById(id);
        }
        if(entity==null){
            entity = new SysOrgEntity();
        }
        model.addAttribute("org", entity);
        return "org/save";
    }
}
