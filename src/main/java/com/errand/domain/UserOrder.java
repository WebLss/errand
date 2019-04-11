package com.errand.domain;

import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

@Table("order_user")
@TableIndexes({@Index(name="orderid_userid", fields={"orderId", "userId"})})
public class UserOrder  implements Serializable {

    @Id
    private Long id;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String orderId;

    @Column
    @ColDefine(type = ColType.INT)
    private Long userId;  // 所创建该订单的用户id


    @Column
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Long sellerId;   // 所接受该订单的卖家id


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", userId=" + userId +
                ", sellerId=" + sellerId +
                '}';
    }
}
