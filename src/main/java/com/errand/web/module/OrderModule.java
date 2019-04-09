package com.errand.web.module;
import com.errand.domain.Order;
import com.errand.mvc.filter.AccessTokenFilter;
import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

/**
 * 订单模块
 */

@IocBean
@At("/api/v1/order")
@Ok("json")
@Chain("crossOrigin")
public class OrderModule {


    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/create")
    @AdaptBy(type = JsonAdaptor.class)
    public Result add(@Param("..") Order order) {

        System.out.println(order.toString());
        return ResponseResult.newFailResult("shibai");
    }

}
