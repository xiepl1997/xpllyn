package com.xpllyn.configurer;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 拦截器
        // 1.一个URL可以设置多个Filter，用逗号隔开
        // 2.当设置多个过滤器时，全部验证通过，才视为通过
        // 3.部分过滤器可以指定参数，如perms，roles
        // 4.anon表示可以匿名访问不过滤；authc表示所有url都必须通过认证才可以访问；user表示记住我或者通过认证可以访问
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("static/**", "anon");
        filterChainDefinitionMap.put("/bootstrap-3.3.7-dist/**", "anon");
        filterChainDefinitionMap.put("/chat/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/font-awesome-4.7.0/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/homepage", "anon");
        filterChainDefinitionMap.put("/homepage.html", "anon");
        filterChainDefinitionMap.put("/GitHubPageSearch/**", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/loginpage", "anon");
        filterChainDefinitionMap.put("/insertMessage", "anon");
        filterChainDefinitionMap.put("/getAllMessages", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/user/**", "anon");
        filterChainDefinitionMap.put("/chatroom/**", "user");
        // 配置登出过滤器
        filterChainDefinitionMap.put("/logout", "logout");
        // 过滤链定义，从上到下顺序执行，一般将/**放在最下面
        // authc：所有url都必须认证通过才可以访问；anon：所有url都可以匿名访问
        filterChainDefinitionMap.put("/**", "user");

        // 如果不设置，会自动寻找web工程目录下的index
        shiroFilterFactoryBean.setLoginUrl("/loginpage");
        shiroFilterFactoryBean.setUnauthorizedUrl("/loginpage");
        shiroFilterFactoryBean.setSuccessUrl("/chatroom");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 凭证匹配器，密码校验给予shiro的SimpleAuthenticationInfo处理
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); //散列算法
        hashedCredentialsMatcher.setHashIterations(2); // 散列次数，相当于md5(md5(""))
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(myShiroRealm());
        //将cookie管理器交给SecurityManager进行管理
        securityManager.setRememberMeManager(rememberMeManager());
        ThreadContext.bind(securityManager);
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 设置cookie
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称,对应前端的checkbox的name=rememberMe
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
        cookie.setHttpOnly(true);
        //有效期为两天
        cookie.setMaxAge(2*24*60*60);
        return cookie;
    }

    /**
     * cookie管理对象，记住我的功能
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥  建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        manager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return manager;
    }
}
