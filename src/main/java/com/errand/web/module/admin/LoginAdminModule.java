package com.errand.web.module.admin;

import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

/**
 * @user: 180296-Web寻梦狮
 * @date: 2018-02-03 11:26
 * @description: 登录模块
 */
@Api(value = "/bgo")
@IocBean
@At("/bgo")
@Ok("json")
@Chain("crossOrigin")
public class LoginAdminModule {

    @At("/login")
    public Result<String> dologin() {

          return ResponseResult.newResult("xiaochuan");
    }

}
