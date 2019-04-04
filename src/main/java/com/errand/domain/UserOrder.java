package com.errand.domain;

import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

@Table("order_user")
@TableIndexes({@Index(name="orderid_userid", fields={"orderId", "userId"})})
public class UserOrder  implements Serializable {

    @Id
    private Long id;

    @Column
    @ColDefine(type = ColType.INT)
    private Long orderId;

    @Column
    @ColDefine(type = ColType.INT)
    private Long userId;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    private boolean type;  // 1.买家订单 2.卖家订单


}
