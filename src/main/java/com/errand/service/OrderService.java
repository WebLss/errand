package com.errand.service;


import com.errand.common.page.Pagination;
import com.errand.domain.Order;
import com.errand.domain.User;
import com.errand.domain.UserOrder;
import com.errand.exception.BusinessException;
import com.errand.web.support.ResponseCodes;
import com.errand.web.support.Result;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.Exps;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.util.ArrayList;
import java.util.List;


/**
 * 订单业务层
 */
@IocBean(args = { "refer:dao" })
public class OrderService extends BaseService<Order>{

    public OrderService(Dao dao) {
        super(dao);
    }

    /**
     * 创建订单
     * @param order object
     * @param userId long
     * @throws Exception
     */
    public void insert(Order order, final Long userId) throws Exception{

        final Order _order = order;
        try {
            Trans.exec(new Atom(){
                public void run() {
                    dao().insert(_order);
                    //Sql sql = Sqls.create("INSERT INTO $table (orderId,userId) VALUES(@orderid,@userid)");
                    // 为变量占位符设值
                    //sql.vars().set("table","order_user");
                    // 为参数占位符设值
                    //sql.params().set("orderid",_order.getId()).set("userid",userId);
                    //dao().execute(sql);
                    UserOrder userOrder = new UserOrder();
                    userOrder.setOrderId(_order.getOrderId());
                    userOrder.setUserId(userId);

                    dao().insert(userOrder);

                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new BusinessException(ResponseCodes.RESPONSE_CODE_SYSTEM_ERROR);
        }

    }

    /**
     * 更新
     * @param order
     * @return
     */
    public int update(Order order) {
        return dao().update(order);
    }

    /**
     * 更新
     * @param order object
     * @param Regx  "^age$" "^age$" //正则
     * @return
     */
    public int update(Order order, String Regx) {

        return dao().update(order, Regx);
    }


    /**
     * 查找
     * @param orderId String
     * @return order of object
     */
    public Order find(String orderId) {
        return dao().fetch(Order.class, orderId);
    }


    public List<Order> list(int status, Long userId, Pagination page) {
        //dao.createPager 第一个参数是第几页，第二参数是一页有多少条记录
        //orderStatus;  // 订单状态 1.待付款 2.待取货 3.待送货 4.待评论 5.已完成 6已取消
        //List<UserOrder> userOrderList = dao().query(UserOrder.class, Cnd.where("userId", "=", userId));
        // 创建一个 Criteria 接口实例
        Criteria cri = Cnd.cri();
        cri.where().and(Exps.inSql("orderId", "select orderId from order_user where userId = "+userId));
        // 组装条件
        if(status == 0) {  // 全部

        } else {
            cri.where().and("orderStatus", "=", status);
        }
        return dao().query(Order.class, cri, dao().createPager(page.getPageNo(), page.getPageSize()));
    }

}
