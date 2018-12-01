package com.mdd.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.IdWorkerUtil;
import com.mdd.admin.config.IgnoreLogin;
import com.mdd.admin.config.Result;
import com.mdd.admin.model.SysPostEntity;
import com.mdd.admin.model.SysUserEntity;
import com.mdd.admin.model.SysUserMenuEntity;
import com.mdd.admin.service.SysPostService;
import com.mdd.admin.service.SysUserMenuService;
import com.mdd.admin.service.SysUserService;
import com.mdd.admin.table.SysUserMenuTable;
import com.mdd.admin.table.SysUserTable;
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
 * @Create 2018/12/1 20:12
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    /**
     * 根据用户名称获取用户职位
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @GetMapping("getPostListByUserName.html")
    @ResponseBody
    @IgnoreLogin
    public Result getPostListByUserName(@NotNull(message = "登录名称不能为空") String userName) throws Exception {
        List<SysPostEntity> sysPostEntities = sysPostService.selectByLoginName(userName);
        return Result.success(sysPostEntities, "根据用户名称获取用户职位");
    }

    /**
     * 界面
     *
     * @return
     * @throws Exception
     */
    @GetMapping("list.html")
    public String list() throws Exception {
        return "user/list";
    }

    /**
     * 获取用户信息
     *
     * @param orgId
     * @return
     * @throws Exception
     */
    @GetMapping("selectUserList.html")
    public String selectUserList(Long orgId, Model model) throws Exception {
        EntityWrapper entityWrapper = new EntityWrapper();
        if (orgId != null) {
            entityWrapper.eq(SysUserTable.ORG_ID, orgId);
        }
        List list = sysUserService.selectList(entityWrapper);
        model.addAttribute("list", list);
        return "user/userList";
    }


    /**
     * 保存界面
     *
     * @return
     * @throws Exception
     */
    @GetMapping("savePage.html")
    public String savePage(Long id, Model model) throws Exception {
        SysUserEntity entity;
        if (id == null) {
            entity = new SysUserEntity();
        } else {
            entity = sysUserService.selectById(id);
        }
        if (entity == null) {
            entity = new SysUserEntity();
        }
        model.addAttribute("user", entity);
        return "user/save";
    }

    /**
     * 保存数据
     *
     * @param sysOrgEntity
     * @return
     * @throws Exception
     */
    @PostMapping("save.html")
    @ResponseBody
    public Result save(SysUserEntity sysOrgEntity) throws Exception {
        boolean flag;
        if (sysOrgEntity.getId() != null) {
            //TODO MD5
            sysOrgEntity.setPwd(null);
            flag = sysUserService.updateById(sysOrgEntity);
        } else {
            sysOrgEntity.setSalt(IdWorkerUtil.getIdString());
            flag = sysUserService.insert(sysOrgEntity);
        }
        if (flag) {
            return Result.success("保存成功");
        }
        return Result.error("保存失败");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("delete.html")
    @ResponseBody
    public Result delete(Long id) throws Exception {
        boolean flag = sysUserService.deleteById(id);
        if (flag) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 权限数据
     *
     * @return
     * @throws Exception
     */
    @GetMapping("authority.html")
    public String authority(Model model, Long userId) throws Exception {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq(SysUserMenuTable.USER_ID, userId);
        List<SysUserMenuEntity> list = sysUserMenuService.selectList(entityWrapper);
        String str = "";
        if (list != null) {
            for (SysUserMenuEntity entity : list) {
                str += "," + entity.getMenuId();
            }
        }
        str = str.replaceFirst(",", "");
        model.addAttribute("str", str);
        model.addAttribute("userId", userId);
        return "user/authority";
    }

    /**
     * 保存权限
     *
     * @return
     */
    @PostMapping("saveAuthority.html")
    @ResponseBody
    public Result saveAuthority(Long[] menuIds, Long userId) {
        sysUserMenuService.save(userId, menuIds);
        return Result.success("保存权限成功");
    }
}