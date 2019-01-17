package com.errand.service;

import com.errand.domain.User;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@IocBean(args = { "refer:dao" })
public class UserService extends BaseService<User> {

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


}
