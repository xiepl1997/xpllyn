package com.xpllyn.configurer;

import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.UserService;
import com.xpllyn.utils.Constant;
import com.xpllyn.utils.EncryptionUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 角色权限和对应权限添加
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    /**
     * 用户认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 加这一步的目的是在post请求的时候会先进行认证，然后再到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        // 获取用户信息
        String email = authenticationToken.getPrincipal().toString();
        User user = userService.findByEmail(email);
        User tempUser = null;
        if (user == null) {
            // 这里返回后会报出对应异常
            return null;
        } else {
            // 处理一个账号异地登录的问题，后期用户数量上来之后需要做优化
            DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
            DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
            // 获取当前已登录的session列表
            Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
            for (Session session : sessions) {
                //查找是否有当前登录账户的记录，有就清除该用户以前登录时保存的session
                Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if (obj != null) {
                    if (obj instanceof SimplePrincipalCollection) {
                        // 强转
                        SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                        tempUser = (User) spc.getPrimaryPrincipal();
                        if (user.getId() == tempUser.getId()) {
                            ctxDisconnect(String.valueOf(user.getId()));
                            sessionManager.getSessionDAO().delete(session);
                        }
                    }
                }
            }

        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, user.getUser_pw(), ByteSource.Util.bytes(EncryptionUtils.salt), getName());
        return simpleAuthenticationInfo;
    }

    // 发送一条关闭websocket连接的消息，让客户端断开连接
    public void ctxDisconnect(String id) {
        ChannelHandlerContext ctx = Constant.onlineUser.get(id);
        ctx.channel().disconnect();
        ctx.channel().close();
        ctx.disconnect();
        ctx.close();
    }
}
