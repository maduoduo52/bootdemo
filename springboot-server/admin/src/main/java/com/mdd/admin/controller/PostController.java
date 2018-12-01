package com.mdd.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.Result;
import com.mdd.admin.model.SysPostEntity;
import com.mdd.admin.model.SysPostMenuEntity;
import com.mdd.admin.model.SysUserEntity;
import com.mdd.admin.model.SysUserPostEntity;
import com.mdd.admin.service.SysPostMenuService;
import com.mdd.admin.service.SysPostService;
import com.mdd.admin.service.SysUserPostService;
import com.mdd.admin.service.SysUserService;
import com.mdd.admin.table.SysPostMenuTable;
import com.mdd.admin.table.SysUserPostTable;
import com.mdd.admin.table.SysUserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 19:55
 */
@Controller
@RequestMapping("post")
public class PostController {

    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostMenuService sysPostMenuService;


    /**
     * 界面
     * @return
     * @throws Exception
     */
    @GetMapping("list.html")
    public String list()throws Exception{
        return "post/list";
    }

    /**
     * @param orgId
     * @return
     * @throws Exception
     */
    @GetMapping("selectList.html")
    public String selectUserList(Long orgId, Model model) throws Exception{
        EntityWrapper entityWrapper = new EntityWrapper();
        if(orgId!=null){
            entityWrapper.eq(SysUserTable.ORG_ID, orgId);
        }
        List list = sysPostService.selectList(entityWrapper);
        model.addAttribute("list", list);
        return "post/postList";
    }


    /**
     * 保存界面
     * @return
     * @throws Exception
     */
    @GetMapping("savePage.html")
    public String savePage(Long id, Model model)throws Exception{
        SysPostEntity entity;
        if(id==null){
            entity = new SysPostEntity();
        }else{
            entity = sysPostService.selectById(id);
        }
        if(entity==null){
            entity = new SysPostEntity();
        }
        model.addAttribute("user", entity);
        return "post/save";
    }

    /**
     * 保存数据
     * @param sysPostEntity
     * @return
     * @throws Exception
     */
    @PostMapping("save.html")
    @ResponseBody
    public Result save(SysPostEntity sysPostEntity)throws Exception{
        boolean flag ;
        if(sysPostEntity.getId()!=null){
            //TODO MD5
            flag = sysPostService.updateById(sysPostEntity);
        }else{
            flag = sysPostService.insert(sysPostEntity);
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
    public Result delete(Long id)throws Exception{
        boolean flag  = sysPostService.deleteById(id);
        if(flag){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 保存用户界面
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("saveUserPage.html")
    public String saveUserPage(@NotNull(message = "参数异常") Long id, Model model) throws Exception{
        List list = sysUserService.selectList(new EntityWrapper<>());
        model.addAttribute("list", list);
        model.addAttribute("postId", id);

        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysUserPostTable.POST_ID, id);
        List<SysUserPostEntity> ls =  sysUserPostService.selectList(entityWrapper);

        List<Long> entities = new ArrayList<>();
        if(ls!=null && !ls.isEmpty()){
            for (SysUserPostEntity l : ls) {
                entities.add(l.getUserId());
            }
        }
        List<SysUserEntity> userList =sysUserService.selectBatchIds(entities);
        model.addAttribute("userList", userList);
        return "post/saveUser";
    }


    /**
     * 保存职位用户
     * @param id
     * @param userIds
     * @return
     * @throws Exception
     */
    @PostMapping("saveUser.html")
    @ResponseBody
    public Result saveUser(@NotNull(message = "参数异常") Long id, Long [] userIds)throws Exception{
        boolean flag = sysUserPostService.saveUser(id,userIds);
        if(flag){
            return Result.success("保存成功");
        }
        return Result.success("保存失败");
    }


    /**
     * 权限数据
     * @return
     * @throws Exception
     */
    @GetMapping("authority.html")
    public String authority(Model model, Long postId)throws Exception{
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysPostMenuTable.POST_ID,postId);
        List<SysPostMenuEntity> list = sysPostMenuService.selectList(entityWrapper);
        String str = "";
        if(list!=null){
            for (SysPostMenuEntity entity : list) {
                str +=","+entity.getMenuId();
            }
        }
        str = str.replaceFirst(",", "");
        model.addAttribute("str", str);
        model.addAttribute("postId", postId);
        return "post/authority";
    }

    /**
     * 保存权限
     * @return
     */
    @PostMapping("saveAuthority.html")
    @ResponseBody
    public Result saveAuthority(Long [] menuIds,Long postId){
        sysPostMenuService.save(postId, menuIds);
        return Result.success("保存权限成功");
    }

}
