package com.mdd.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mdd.admin.config.*;
import com.mdd.admin.config.constant.LoginConstant;
import com.mdd.admin.config.redis.RedisTemplateUtils;
import com.mdd.admin.model.SysMenuEntity;
import com.mdd.admin.model.SysPostMenuEntity;
import com.mdd.admin.model.SysUserEntity;
import com.mdd.admin.model.SysUserMenuEntity;
import com.mdd.admin.service.SysMenuService;
import com.mdd.admin.service.SysPostMenuService;
import com.mdd.admin.service.SysUserMenuService;
import com.mdd.admin.service.SysUserService;
import com.mdd.admin.table.SysPostMenuTable;
import com.mdd.admin.table.SysUserMenuTable;
import com.mdd.admin.table.SysUserTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 19:31
 */
@Controller
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    @Autowired
    private SysPostMenuService sysPostMenuService;

    @Autowired
    private LoginConfig loginConfig;


    /**
     * 登录界面
     *
     * @return
     * @throws Exception
     */
    @GetMapping("login.html")
    @IgnoreLogin
    public String loginPage() throws Exception {
        return "login";
    }

    /**
     * 登录界面
     *
     * @return
     * @throws Exception
     */
    @GetMapping("main.html")
    @IgnoreLogin
    public String mian() throws Exception {
        return "main";
    }


    /**
     * 根目录
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/")
    @IgnoreLogin
    public String index() throws Exception {
        if (Constant.getEmpId() == null) {
            return "redirect:/login.html";
        }
        return "redirect:/index.html";
    }

    /**
     * 首页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index.html", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT})
    public String index2(Model model) throws Exception {
        SysUserEntity sysUserEntity = Constant.get().getSysUserEntity();
        List<SysMenuEntity> sysMenuEntities = new ArrayList<>();
        Set<String> sets = new HashSet<>();
        Set<String> buttons = new HashSet<>();
        if (sysUserEntity.getAdmin() != null && sysUserEntity.getAdmin()) {
            sysMenuEntities = sysMenuService.selectList(new EntityWrapper<>());
            sysMenuEntities = chuli(sysMenuEntities, sets, buttons);
        } else {
            List<Long> menuIds = new ArrayList<>();

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq(SysUserMenuTable.USER_ID, Constant.getEmpId());
            List<SysUserMenuEntity> sysUserMenuEntities = sysUserMenuService.selectList(wrapper);
            if (sysUserMenuEntities != null) {
                for (SysUserMenuEntity sysUserMenuEntity : sysUserMenuEntities) {
                    menuIds.add(sysUserMenuEntity.getMenuId());
                }
            }
            wrapper = new EntityWrapper();
            wrapper.eq(SysPostMenuTable.POST_ID, Constant.get().getPostId());
            List<SysPostMenuEntity> sysPostMenuEntities = sysPostMenuService.selectList(wrapper);
            if (sysPostMenuEntities != null) {
                for (SysPostMenuEntity sysPostMenuEntity : sysPostMenuEntities) {
                    menuIds.add(sysPostMenuEntity.getMenuId());
                }
            }
            sysMenuEntities = sysMenuService.selectBatchIds(menuIds);
            sysMenuEntities = chuli(sysMenuEntities, sets, buttons);
        }
        model.addAttribute("sysMenuEntities", sysMenuEntities);
        model.addAttribute("buttons", buttons);
        //不是超级管理员
        if (sysUserEntity.getAdmin() == null || !sysUserEntity.getAdmin()) {
            String key = LoginConstant.USER_AUTH_KEY + Constant.getEmpId() + "." + Constant.get().getPostId();
            redisTemplateUtils.set(key, JSON.toJSONString(sets), loginConfig.time);
        }
        model.addAttribute("username", Constant.get().getSysUserEntity().getName());
        return "index";
    }

    private List<SysMenuEntity> chuli(List<SysMenuEntity> sysMenuEntities, Set<String> sets, Set<String> buttons) {
        List<SysMenuEntity> list = new ArrayList<>();
        //取出根
        SysMenuEntity gen = null;
        Map<Long, List<SysMenuEntity>> map = new HashMap<>();
        for (SysMenuEntity sysMenuEntity : sysMenuEntities) {
            if (sysMenuEntity.getRootFlag()) {
                gen = sysMenuEntity;
            }
            Long pid = sysMenuEntity.getParent();
            List<SysMenuEntity> entities = map.get(pid);
            if (entities == null) {
                entities = new ArrayList<>();
            }
            entities.add(sysMenuEntity);
            map.put(pid, entities);

            String authorityUrl = sysMenuEntity.getAuthorityUrl();
            String authorityButton = sysMenuEntity.getAuthorityButton();
            if (!StringUtils.isEmpty(authorityUrl)) {
                String authorityUrls[] = authorityUrl.split(",");
                for (String url : authorityUrls) {
                    sets.add(url);
                }
            }
            if (!StringUtils.isEmpty(authorityButton)) {
                String authorityButtons[] = authorityButton.split(",");
                for (String button : authorityButtons) {
                    buttons.add(button);
                }
            }

        }
        for (SysMenuEntity sysMenuEntity : sysMenuEntities) {
            if (gen.getId().equals(sysMenuEntity.getParent())) {
                sysMenuEntity.setSysMenuEntities(map.get(sysMenuEntity.getId()));
                list.add(sysMenuEntity);
            }
        }
        return list;
    }

    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    @PostMapping("login.html")
    @IgnoreLogin
    @ResponseBody
    public Result login(String username, String pwd, String postId, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(postId)) {
            return Result.error("请选择职位！");
        }
        if (StringUtils.isEmpty(username)) {
            return Result.error("请输入登录名！");
        }
        if (StringUtils.isEmpty(pwd)) {
            return Result.error("请输入密码！");
        }

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq(SysUserTable.LOGIN_NAME, username);
        SysUserEntity sysUserEntity = sysUserService.selectOne(wrapper);
        if (sysUserEntity == null) {
            return Result.error("错误的用户名或者密码！");
        }
        //密码解密
        String md5Str = MD5Utils.encryptByMD5(pwd + sysUserEntity.getSalt());
        if (!md5Str.equals(sysUserEntity.getPwd())) {
            return Result.error("错误的用户名或者密码！");
        }
        //TODO 权限
        String token = IdWorkerUtil.getIdString();
        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(sysUserEntity.getId()));
        map.put("postId", postId);
        map.put("token", token);
        String key = LoginConstant.LOGIN_KEY + sysUserEntity.getId() + "." + token;
        if (loginConfig.owner) {
            redisTemplateUtils.del(key + "*");
        }
        //用户信息加密
        String aesStr = AESCodeUtil.encryptAES(JSON.toJSONString(map), loginConfig.aesKey);
        redisTemplateUtils.set(key, JSON.toJSONString(sysUserEntity), loginConfig.time);
        //cookie设置为用户信息
        Cookie cookie = new Cookie(LoginConstant.LOGIN_COOKIE, aesStr);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(loginConfig.time));
        response.addCookie(cookie);
        return Result.success("登录成功");
    }

    /**
     * 删除
     *
     * @return
     * @throws Exception
     */
    @GetMapping("logOut.html")
    public String logOut() throws Exception {
        String key = LoginConstant.LOGIN_KEY + Constant.getEmpId() + "." + Constant.get().getToken();
        redisTemplateUtils.del(key);
        return "redirect:/login.html";
    }

}
