package com.errand.web.module;
import com.errand.common.page.Pagination;
import com.errand.domain.Order;
import com.errand.domain.User;
import com.errand.exception.BusinessException;
import com.errand.mvc.context.UserContext;
import com.errand.mvc.filter.AccessTokenFilter;
import com.errand.service.OrderService;
import com.errand.service.UserService;
import com.errand.utils.CommonUtils;
import com.errand.web.support.ResponseResult;
import com.errand.web.support.Result;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.adaptor.JsonAdaptor;
import org.nutz.mvc.annotation.*;

import javax.xml.ws.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单模块
 */

@IocBean
@At("/api/v1/order")
@Ok("json")
@Chain("crossOrigin")
public class OrderModule {


    @Inject
    protected OrderService orderService;

    @Inject
    protected UserService userService;

    /**
     * 创建订单
     * @param order object
     * @return Result
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/create")
    @AdaptBy(type = JsonAdaptor.class)
    public Result create(@Param("..") Order order) {
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        try {
            order.setOrderId(CommonUtils.getOrderIdByTime());
            order.setCreateDate(new Date());
            order.setOrderStatus(1); // 待支付状态
            orderService.insert(order, user.getId());

        } catch (Exception e) {
            return ResponseResult.newFailResult("订单创建失败");
        }
        return ResponseResult.newResult(order);
    }

    /**
     * 模拟支付
     * @param orderId id
     * @return Result
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/toPay")
    @AdaptBy(type = JsonAdaptor.class)
    public Result toPay(@Param("orderId") String orderId) {
        Order order = orderService.find(orderId);
        if(order != null && order.getOrderStatus() == 1) {
            order.setOrderStatus(2); // 设置为待取货状态
            order.setCreateDate(new Date());
            int count = orderService.update(order, "^orderStatus$");
            if(count > 0) {
                return ResponseResult.newResult(order.getOrderId());  // 若支付成功则返回订单号
            } else {
                return ResponseResult.newFailResult("支付失败！");
            }
        } else {
            return ResponseResult.newFailResult("不存在该支付订单");
        }
    }

    /**
     *
     * @param status 订单状态
     * @param pageNo 当前页码
     * @param pageSize 每页显示多少条数据
     * @return Result
     */
    @GET
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/list")
    @AdaptBy(type = JsonAdaptor.class)
    public Result list(@Param("status") int status, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize) {
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        Pagination page = new Pagination();
        if(pageNo == 0) pageNo = 1;
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        List<Order> list = orderService.list(status, user.getId(), page);
        page.setList(list);
        //page.setTotalCount(list.size());
        return ResponseResult.newResult(page);
    }


    @GET
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/find")
    @AdaptBy(type = JsonAdaptor.class)
    public Result find(@Param("orderId") String orderId) {
        if(orderId != null) {
            return ResponseResult.newResult(orderService.find(orderId));
        }
        return ResponseResult.newFailResult("地址id无效");
    }

    /**
     * 客户确认收货
     * @param orderId string
     * @return result
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/confirmOrder")
    @AdaptBy(type = JsonAdaptor.class)
    public Result confirmOrder(@Param("orderId") String  orderId) {
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        if(orderId != "") {
            Order order = orderService.find(orderId);
            order.setOrderStatus(4);
            order.setFinish_time(new Date().toString());
            try {
                orderService.confirmOrder(order, user.getId());
                return ResponseResult.newResult();
            } catch (BusinessException e) {
                return ResponseResult.newFailResult(e.getMessage());
            }

            /*if(orderService.update(order, "^orderStatus$") > 0) {
                return ResponseResult.newResult();
            } else {
                return ResponseResult.newFailResult("确认收货失败");
            }*/
        } else {
            return ResponseResult.newFailResult("无效订单号");
        }
    }


    /**
     * 配送员
     * 抢单列表
     * @param status 订单状态
     * @param pageNo 当前页码
     * @param pageSize 每页显示多少条数据
     * @return Result
     */
    @GET
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/gradList")
    @AdaptBy(type = JsonAdaptor.class)
    public Result gradList(@Param("status") int status, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize) {
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        // 不是超级管理员，但是是业务员却不是vip的情况下
        if(!user.isSuper() && user.isTaker() && !user.isVip()) {
            //时间格式化
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if(!CommonUtils.isToday(user.getIsTipTime())) { //时间输出为true，则处于当天24h范围内，false反之
                    user.setIsAble(5);  // 设置可抢单5次
                    user.setIsTipTime(new Date());
                    userService.update(user);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        Pagination page = new Pagination();
        if(pageNo == 0) {
            pageNo = 1;
        }
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        List<Order> list = null;
        if(status == 3) {
            list = orderService.list(status, user.getId(), page, 1);  // seller
        } else {
            list = orderService.list(status, (long) 0, page);
        }

        page.setList(list);
        return ResponseResult.newResult(page);
    }


    /**
     * 配送员确认接单
     * @param orderId string
     * @return result
     */
    @POST
    @Filters(@By(type = AccessTokenFilter.class, args = {"ioc:tokenFilter"}))
    @At("/confirm")
    @AdaptBy(type = JsonAdaptor.class)
    public Result confirm(@Param("orderId") String orderId) {
        User user = UserContext.getCurrentuser().get();
        user = userService.fetchByCnd(Cnd.where("name","=", user.getName()).and("password", "=", user.getPassword()));
        try {
            orderService.receiveOrder(orderId, user);
            return ResponseResult.newResult();
        } catch (BusinessException e) {
            return ResponseResult.newFailResult(e.getMessage());
        }
    }



}
