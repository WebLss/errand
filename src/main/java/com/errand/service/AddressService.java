package com.errand.service;

import com.errand.domain.Address;
import com.errand.domain.User;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.util.ArrayList;

/**
 * 地址管理业务类
 */

@IocBean(args = { "refer:dao" })
public class AddressService extends BaseService<Address> {



    public AddressService(Dao dao) {
        super(dao);
    }

    public void insert(final Address address) {


        Trans.exec(new Atom(){
            public void run() {
                dao().insert(address);
                System.out.println(address.toString());
                dao().insertRelation(Address.class, "users");
            }
        });
    }


}
