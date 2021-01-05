package com.xpllyn.configurer;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * 2020-6-26 Peiliang Xie
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在controller调用之前进行调用
     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.sendRedirect(request.getContextPath() + "/");
//        return false;
//    }
}
