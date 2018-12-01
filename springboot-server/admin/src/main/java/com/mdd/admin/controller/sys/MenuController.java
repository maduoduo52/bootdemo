package com.mdd.admin.controller.sys;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.IdWorkerUtil;
import com.mdd.admin.config.Result;
import com.mdd.admin.model.SysMenuEntity;
import com.mdd.admin.service.SysMenuService;
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
 * @Create 2018/12/1 19:53
 */
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 界面
     * @return
     * @throws Exception
     */
    @GetMapping("list.html")
    public String list()throws Exception{
        return "menu/list";
    }


    /**
     * 获取全部数据
     * @return
     * @throws Exception
     */
    @GetMapping("getAll.html")
    @ResponseBody
    public Result getAll()throws Exception{
        List<SysMenuEntity> list = sysMenuService.selectList(new EntityWrapper<>());
        return Result.success(list,"菜单数据");
    }

    /**
     * 查询菜单信息
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("getById.html")
    @ResponseBody
    public Result getById(@NotNull(message = "id不能为空") Long id)throws Exception{
        return Result.success(sysMenuService.selectById(id),"查询菜单信息");
    }

    /**
     * 保存界面
     * @return
     * @throws Exception
     */
    @GetMapping("savePage.html")
    public String savePage(Long id, Model model)throws Exception{
        SysMenuEntity entity;
        if(id==null){
            entity = new SysMenuEntity();
        }else{
            entity = sysMenuService.selectById(id);
        }
        if(entity==null){
            entity = new SysMenuEntity();
        }
        model.addAttribute("menu", entity);
        return "menu/save";
    }

    /**
     * 保存数据
     * @param entity
     * @return
     * @throws Exception
     */
    @PostMapping("save.html")
    @ResponseBody
    public Result save(SysMenuEntity entity)throws Exception{
        boolean flag ;
        if(entity.getId()!=null){
            entity.setParent(null);
            flag = sysMenuService.updateById(entity);
        }else{
            entity.setId(IdWorkerUtil.getId());
            flag = sysMenuService.insert(entity);
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
        boolean flag  = sysMenuService.deleteById(id);
        if(flag){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
