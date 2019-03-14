package com.errand.service;

import com.errand.domain.Address;
import com.errand.exception.BusinessException;
import com.errand.web.support.ResponseCodes;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

/**
 * 地址管理业务类
 */

@IocBean(args = { "refer:dao" })
public class AddressService extends BaseService<Address> {



    public AddressService(Dao dao) {
        super(dao);
    }

    public void insert(final Address address, final Long userId) throws Exception{

        try {
            Trans.exec(new Atom(){
                public void run() {
                    dao().insert(address);
                    Sql sql = Sqls.create("INSERT INTO $table (addressid,userid) VALUES(@addressid,@userid)");
                    // 为变量占位符设值
                    sql.vars().set("table","address_user");
                    // 为参数占位符设值
                    sql.params().set("addressid",address.getId()).set("userid",userId);
                    dao().execute(sql);
                }
            });
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new BusinessException(ResponseCodes.RESPONSE_CODE_SYSTEM_ERROR);
        }

    }


}
