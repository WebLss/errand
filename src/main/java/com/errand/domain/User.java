package com.errand.domain;


import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 后台用户管理实体类
 */
@Table("system_user")
@TableIndexes({ @Index(name = "user_name", fields = { "name" }, unique = true)})
public class User implements Serializable {

    private static final long serialVersionUID = -965829144356813385L;

    @Id
    private Long id;    // 逻辑id

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String name;  // 用户名

    @Column
    @Default("")
    @ColDefine(type = ColType.CHAR, width = 44)
    private String password;   // 密码

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String nickName;   // 昵称

    @ColDefine(type = ColType.VARCHAR, width = 140)
    private String openId;   // 绑定的weixin 唯一识别号

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String description;  // 描述内容

    @Column(hump = true)
    @ColDefine(type = ColType.TIMESTAMP)
    private Date createDate;

    @Column(hump = true)
    @ColDefine(type = ColType.VARCHAR, width = 15)
    private String registerIp;   // 注册ip地址

    @ManyMany(target = Role.class, relation = "system_user_role", from = "userid", to = "roleid")
    private List<Role> roles;

    @Column("is_locked")
    @Default("true")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean locked;  // 是否冻结账户

    @Column("is_system")
    @Default("false")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean system;   // 是否是后台系统管理人员

    @Column("is_taker")
    @Default("false")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean taker;   // 是否是接单员

    @Column("is_super")
    @Default("false")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean isSuper;   // 是否是超级管理员



    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTaker() {
        return taker;
    }

    public void setTaker(boolean taker) {
        this.taker = taker;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean aSuper) {
        isSuper = aSuper;
    }
}