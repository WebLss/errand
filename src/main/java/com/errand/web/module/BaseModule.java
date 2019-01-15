package com.errand.web.module;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.util.NutMap;

/**
 * @user: 180296-Web寻梦狮
 * @date: 2018-01-18 18:58
 * @description: 基础抽象类
 */
public abstract class BaseModule {
    /** 注入与属性同名的一个ioc对象 */
    @Inject
    protected Dao dao;

 /*   @Inject
    protected EmailService emailService;*/

/*    protected QueryResult query(Class<?> klass, Condition cnd, Pager pager, String regex) {
        if (pager != null && pager.getPageNumber() < 1) {
            pager.setPageNumber(1);
        }
        List<?> roles = dao.query(klass, cnd, pager);
        dao.fetchLinks(roles, null);
        pager.setRecordCount(dao.count(klass, cnd));
        return new QueryResult(roles, pager);
    }*/

    protected NutMap ajaxOk(Object data) {
        return new NutMap().setv("status", true).setv("data", data);
    }

    protected NutMap ajaxFail(String msg) {
        return new NutMap().setv("status", false).setv("msg", msg);
    }
}
