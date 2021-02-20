package com.xpllyn.configurer;

import com.xpllyn.im.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Scope("singleton")
public class AppContext {
    @Autowired
    private NettyServer nettyServer;

    private Thread nettyThread;

    /**
     * 该方法在Constructor,Autowired之后执行
     * 启动netty websocket服务器
     */
    @PostConstruct
    public void init() {
        nettyThread = new Thread(nettyServer);
        nettyThread.start();
    }

    @SuppressWarnings("deprecation")
    @PreDestroy
    public void close() {
        nettyServer.close();
        nettyThread.stop();
    }
}
