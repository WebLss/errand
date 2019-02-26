package com.errand.domain;

import org.nutz.dao.entity.annotation.*;

import java.util.List;

/**
 * 地址保存
 */
@Table("address")
@TableIndexes({ @Index(name = "user_name", fields = { "name" }, unique = true)})
public class Address {

    private static final long serialVersionUID = -965829144356813385L;

    @Id
    private Long id;    // 逻辑id

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String areaName;  // 地址名称

    @Column
    @ColDefine(type = ColType.FLOAT)
    private float longitude;  // 经度

    @Column
    @ColDefine(type = ColType.FLOAT)
    private float latitude;  // 纬度

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String phone;    // 手机号

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String adcode;   // 行政区划代码

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String name;  // 姓名


    @ManyMany(target = Role.class, relation = "address_user", from = "addressid", to = "userid")
    private List<User> user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
