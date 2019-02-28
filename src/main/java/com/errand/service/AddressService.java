package com.errand.service;

import com.errand.domain.Address;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * 地址管理业务类
 */

@IocBean(args = { "refer:dao" })
public class AddressService extends BaseService<Address> {


    public void insert(Address address, int userId) {

    }


}
