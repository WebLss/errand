package com.errand.service;


import com.errand.common.page.Pagination;
import com.errand.domain.User;
import com.errand.domain.UserInfo;
import com.errand.exception.BusinessException;
import com.errand.web.support.ResponseCodes;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.Exps;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.util.ArrayList;
import java.util.List;

@IocBean(args = { "refer:dao" })
public class AdminService extends BaseService<UserInfo>{

    public AdminService(Dao dao) {
        super(dao);
    }

    public void insert(UserInfo userInfo) {
        dao().insert(userInfo);
    }

    public List<UserInfo> list(Long userId, Pagination page) {
        //Criteria cri = Cnd.cri();

       // cri.getOrderBy().desc("id");
        List<UserInfo> userInfos = dao().query(UserInfo.class, null, dao().createPager(page.getPageNo(), page.getPageSize()));
        return userInfos;
    }


    public int query(Long userId) {
        return dao().query(UserInfo.class, Cnd.where("userId", "=", userId)).size();
    }

    public List<NutMap> getExamList() {
        List<UserInfo> userInfos = dao().query(UserInfo.class, Cnd.where("is_exam", "=", 0));

        List<NutMap> nutMaps = new ArrayList<>() ;
        for(UserInfo userInfo: userInfos) {
            System.out.println("==========================="+userInfo.getUserId());
            NutMap nutMap = new NutMap();
            User user = dao().fetch(User.class, userInfo.getUserId());
            nutMap.setv("name", user.getName());
            nutMap.setv("phone", userInfo.getPhone());
            nutMap.setv("userId",userInfo.getUserId());
            nutMap.setv("is_exam", userInfo.isExam());
            nutMap.setv("reason", userInfo.getReason());
            nutMap.setv("id", userInfo.getId());
            nutMaps.add(nutMap);
        }
        return nutMaps;
    }

    /**
     * 通过操作
     */
    public void pass(Long id) throws BusinessException{
        final UserInfo userInfo = dao().fetch(UserInfo.class,id);
        userInfo.setExam(true);
        try {
            Trans.exec(new Atom() {
                @Override
                public void run() {
                    dao().update(userInfo, "^isExam$");
                    dao().update(User.class, Chain.make("taker", true),Cnd.where("id", "=", userInfo.getUserId()));
                }
            });
        } catch (BusinessException e) {
            throw new BusinessException(ResponseCodes.RESPONSE_CODE_SYSTEM_ERROR);
        }

    }

}
