package com.errand.mvc.context;


/**
 * @user: 180296-Web寻梦狮
 * @date: 2018-02-03 16:17
 * @description: 线程中保存token
 */
public class UserContext {
    /*static final ThreadLocal<UserDb> currentUser = new ThreadLocal<UserDb>();
    //用来保存信息的
    static final ThreadLocal<String> loginInfo = new ThreadLocal<String>();

    public UserContext(String logininfo){
        loginInfo.set(logininfo);
    }
    public static ThreadLocal<String> getLoginInfo(){
        return loginInfo;
    }
    public UserContext(UserDb user, String logininfo){
        loginInfo.set(logininfo);
        currentUser.set(user);
    }


    public UserContext(UserDb user){
        currentUser.set(user);
    }
    public static ThreadLocal<UserDb> getCurrentuser() {
        return currentUser;
    }
    public void close() throws Exception {
        currentUser.remove();
        loginInfo.remove();
    }*/
}
