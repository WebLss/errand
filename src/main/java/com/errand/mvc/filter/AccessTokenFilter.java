package com.errand.mvc.filter;
import com.errand.domain.User;
import com.errand.mvc.context.UserContext;
import com.errand.security.JwtToken;
import com.errand.utils.WebUtils;
import com.errand.web.support.ResponseResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.VoidView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @Author :Web寻梦狮（lishengsong）
 * @Date Created in 下午12:09 2018/2/4
 * @Description:
 */
@IocBean(name = "tokenFilter")
public class AccessTokenFilter implements ActionFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    public View match(ActionContext actionContext) {
        System.out.println("AccessTokenFilter");
        HttpServletResponse response = actionContext.getResponse();
        HttpServletRequest request = actionContext.getRequest();
        //response.setCharacterEncoding("UTF-8");
        try {
           // request.setCharacterEncoding("UTF-8");
            if(!checkToken(request,response)) return new VoidView();
        } catch (Exception e) {
            Object result = ResponseResult.newFailResult(e.getMessage());
            try {
                response.getWriter().write(Json.toJson(result));
            } catch (IOException e1) {
                throw new RuntimeException(e1.getMessage());
                //e1.printStackTrace();
            }
            return new VoidView();
        }

        return null;
    }

    private boolean checkToken(HttpServletRequest request , HttpServletResponse response) throws IOException {
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String arg : args) {
            if (arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp")) {
                return true;
            }
        }
        String authorization = WebUtils.getHeaderFromRquest(request, "Authorization");
        System.out.println(authorization);
        if (authorization == null) {
            response.getWriter().write(Json.toJson(ResponseResult.newFailResult("未登录")));
            return false;
        }
        try {
            Claims claims = JwtToken.parseToken(authorization);
            User user = new User();
            user.setName((String) claims.get("name"));
            user.setPassword((String) claims.get("password"));
            UserContext.getCurrentuser().set(user);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
           /* if (e instanceof ExpiredJwtException) { //签名过期
                resultBean.setCode(ResponseResult.LOGIN_EXPIRED);
                resultBean.setMsg("expired login");
                System.out.println("ExpiredJwtException");
            } else {
                resultBean.setCode(ResponseResult.NO_LOGIN);
                resultBean.setMsg("no login");
                System.out.println("Other JwtException");
            }

            response.getWriter().write(Json.toJson(resultBean));*/
            return false;
        }
    }
}
