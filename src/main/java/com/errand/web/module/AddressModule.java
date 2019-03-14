package com.errand.web.module;

import com.errand.domain.Address;
import com.errand.domain.User;
import com.errand.mvc.context.UserContext;
import com.errand.mvc.filter.AccessTokenFilter;
import com.errand.service.AddressService;
import com.errand.service.UserService;
import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import javax.xml.ws.Response;

/**
 * @user: 180296-Web寻梦狮
 * @description: 登录模块
 */

@IocBean
@At("/api/v1/address")
@Ok("json")
@Chain("crossOrigin")
public class AddressModule {

    @Inject
    protected AddressService addressService;

    @Inject
    protected UserService userService;

    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/add")
    @AdaptBy(type = JsonAdaptor.class)
    public Result add(@Param("..") Address addr)  {
        System.out.println(addr.toString());
        User user = UserContext.getCurrentuser().get();
        System.out.println(user.toString());

        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        System.out.println(addr.toString());

        try {
            addressService.insert(addr, user.getId());
        } catch (Exception e) {
            return ResponseResult.newFailResult("新增地址失败");
        }
        return ResponseResult.newResult();
    }






}
