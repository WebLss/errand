package com.errand.utils;




import org.nutz.http.Http;
import org.nutz.http.Response;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;


import java.util.HashMap;
import java.util.Map;

/**
 * 获取微信小程序openId
 */
public class GetOpenIDUtil {

    public final static String appID = "wx82ebc13a30181c3c";
    public final static String appSecret = "2c3bf025d41aa7a5ad4dbe39c45f30ba";

    // 网页授权接口
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";
    public  static NutMap oauth2GetOpenid(String appid, String code, String appsecret) {
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
        Response response = null;
        response = Http.get(requestUrl);
        System.out.println(response.getContent());

        String resString = response.getContent();
        NutMap resJson = Json.fromJson(NutMap.class, resString);

        return resJson;
    }


    /*public static void main(String args[]) {

        oauth2GetOpenid(appID,"033SiiXY1QwtwS0qUcVY1mVdXY1SiiXE",appSecret);

    }*/
}
