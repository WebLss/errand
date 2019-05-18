package com.errand.web.module;

import com.errand.domain.User;
import com.errand.domain.UserInfo;
import com.errand.exception.BusinessException;
import com.errand.mvc.context.UserContext;
import com.errand.mvc.filter.AccessTokenFilter;
import com.errand.service.AdminService;
import com.errand.service.UserService;
import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

@IocBean
@At("/api/v1/adm")
@Ok("json")
@Chain("crossOrigin")
public class AdminModule {

    @Inject
    protected UserService userService;

    @Inject
    protected AdminService adminService;


    /**
     * 申请成为配送员
     * @param userInfo
     * @return
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/add")
    @AdaptBy(type = JsonAdaptor.class)
    public Result add(@Param("..") UserInfo userInfo)  {
        System.out.println(userInfo.toString());
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        System.out.println(user.toString());
        if(adminService.query(user.getId()) > 0) {
            return ResponseResult.newFailResult("您已经申请过，不能重复申请");
        }

        userInfo.setUserId(user.getId());
        try {
            adminService.insert(userInfo);
        } catch (Exception e) {
            return ResponseResult.newFailResult("申请单已提交");
        }
        return ResponseResult.newResult();
    }


    /**
     * 超级管理员获取用户列表
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/getUserList")
    @AdaptBy(type = JsonAdaptor.class)
    public Result getUserList() {
        return ResponseResult.newResult(userService.query());
    }


    /**
     * 获取审核列表
     * @return
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/getExamList")
    @AdaptBy(type = JsonAdaptor.class)
    public Result getExamList() {
        return ResponseResult.newResult(adminService.getExamList());
    }


    /**
     * 申请通过
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/pass")
    @AdaptBy(type = JsonAdaptor.class)
    public Result passExam(@Param("id") Long id) {
        try {
            adminService.pass(id);
            return ResponseResult.newResult("操作成功");
        } catch (BusinessException e) {
            return ResponseResult.newFailResult("操作失败");
        }


    }

    /**
     * 设置vip
     * @param id
     * @return
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/vip")
    @AdaptBy(type = JsonAdaptor.class)
    public Result setVip(@Param("id") Long id) {
        User user = userService.fetchById(id);
        user.setVip(true);
        userService.update(user, "^isVip$");
        return ResponseResult.newResult();
    }

    /**
     * 移除vip
     * @param id
     * @return
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/removeVip")
    @AdaptBy(type = JsonAdaptor.class)
    public Result removeVip(@Param("id") Long id) {
        User user = userService.fetchById(id);
        user.setVip(false);
        userService.update(user, "^isVip$");
        return ResponseResult.newResult();
    }

}
