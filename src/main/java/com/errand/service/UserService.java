package com.errand.service;

import com.errand.domain.User;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;

import java.util.List;

/**
 * User类的业务操作
 * create - 当对象被创建时触发
 * fetch - 当对象被从容器中取出时触发
 * depose - 当对象被销毁时触发
 */
@IocBean(args = { "refer:dao" })
public class UserService extends BaseService<User> {

    public UserService(Dao dao) {
        super(dao);
    }

    public List<User> list() {
        return query(null, null);
    }

    public void update(User user) {
        dao().update(user);
    }

    public void insert(User user) {
        user = dao().insert(user);
        // dao().insertRelation(user, "roles");
        System.out.println(user);
    }

    /**
     * 根据用户名查询用户信息
     * @param name
     * @return
     */
    public User fetchByName(String name) {
        return fetch(Cnd.where("name", "=", name));
    }

    public User fetchByCnd(Condition cnd) {
        return fetch(cnd);
    }

}
