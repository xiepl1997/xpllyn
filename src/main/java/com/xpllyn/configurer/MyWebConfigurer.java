package com.xpllyn.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 过滤器
 * 2020-6-26 Peiliang Xie
 */
@Configuration
public class MyWebConfigurer implements WebMvcConfigurer {

//    /**
//     * 配置视图映射
//     * @param registry
//     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//
//    }
//
//    /**
//     * 配置拦截路径，拦截controller中的请求路径
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/userInfo")
//                .excludePathPatterns("/", "/login", "/login/**", "/register", "static/**", "bootstrap-3.3.7-dist/**",
//                        "css/**", "font-awesome-4.7.0/**", "images/**", "js/**", "templates/**");
//    }
}
