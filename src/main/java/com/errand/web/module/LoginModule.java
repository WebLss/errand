package com.errand.web.module;

import com.errand.domain.User;
import com.errand.security.JwtTonken;
import com.errand.service.UserService;
import com.errand.utils.GetOpenIDUtil;
import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.xml.ws.Response;


/**
 * @user: 180296-Web寻梦狮
 * @description: 登录模块
 */
@IocBean
@At("/api/v1/login")
@Ok("json")
@Chain("crossOrigin")
public class LoginModule {

    @Inject
    protected UserService userService;

    @POST
    //@Filters(@By(type = CrossOriginsFilter.class,args = {"ioc:crossFilter"}))
    @At("/login")
    public Result doLogin(@Param("username")  String name, @Param("password") String password) {

        return this.validUser(name, password);
    }


    /**
     * 绑定用户信息
     * @param role 角色role
     * @param name 用户名
     * @param password 密码
     * @param code 微信小程序的code用于获取openId
     * @return
     */
    @At("/bindUser")
    public Result bindUser(@Param("role") String role, @Param("username") String name , @Param("password") String password,
                           @Param("code") String code) {

        Result res =  this.validUser(name, password, role);
        if(res != null) return res;
        if (code == null || code.length() == 0) {
            return ResponseResult.newFailResult("code不能为空");
        } else {
            Result result = this.validUser(name, password);
            if(result.getStatus() == 0) {
                NutMap resultMap = GetOpenIDUtil.oauth2GetOpenid(GetOpenIDUtil.appID, code, GetOpenIDUtil.appSecret);
                if(resultMap.has("errcode")) {
                    return ResponseResult.newFailResult(resultMap.getString("errmsg"));
                } else {
                    System.out.println(resultMap);
                }
            }
        }
        return null;
    }

    /**
     * 验证用户数据
     * @param name 用户名
     * @param password 密码
     * @return
     */
    public Result validUser(String name, String password) {
        if(Strings.isBlank(name) || Strings.isBlank(password)){
            return ResponseResult.newFailResult("用户名或密码不能为空");
        }
        User exist = userService.fetchByName(name);
        if (exist == null) {
            return ResponseResult.newFailResult("用户名不存在");
        }



        User user = userService.fetchByName(name);
        if (user != null) {
            if(user.getPassword().equalsIgnoreCase(Lang.md5(password))) {
                return ResponseResult.newResult(user).setHeader("token", JwtTonken.createToken(user));
            } else {
                return ResponseResult.newFailResult("登录密码错误");
            }
        } else {
            return ResponseResult.newFailResult("不存在该用户");
        }
    }

    /**
     * 验证用户数据
     * @param names 用户名
     * @param passwords 密码
     * @param role 角色
     * @return
     */
    public Result validUser(String names, String passwords, String role) {
        if(Strings.isBlank(names) || Strings.isBlank(passwords)){

            return ResponseResult.newFailResult("用户名或密码不能为空");

        }
        User exist = userService.fetchByName(names);
        if (exist == null) {

            return ResponseResult.newFailResult("用户名不存在");
        }

        if(role != null) {
            String message = "";
            switch (role) {
                case "A":
                    System.out.println("A");
                    if(!exist.isSuper()) {
                        message = "该角色下不存在对应用户";
                    }
                    break;
                case "B":
                    System.out.println("B");
                    if(!exist.isSystem()) {
                        message = "该角色下不存在对应用户";
                    }
                default:
                    break;
            }
            return ResponseResult.newFailResult(message);
        }

        return null;
    }


}
