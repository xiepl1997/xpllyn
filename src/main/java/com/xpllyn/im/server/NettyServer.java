package com.xpllyn.im.server;

import com.xpllyn.im.handler.HttpRequestHandler;
import com.xpllyn.im.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * netty websocket 服务器
 * 使用独立线程启动
 */
@Component
public class NettyServer implements Runnable {
    private int port = 3333;

    private ServerBootstrap bootstrap;

    private ChannelFuture serverChannelFuture;

    private NioEventLoopGroup boss;

    private NioEventLoopGroup worker;

    @Autowired
    private HttpRequestHandler httpRequestHandler;

    @Autowired
    private WebSocketServerHandler webSocketServerHandler;

    public NettyServer() {}

    public void start() {

        // 创建boss和worker两个线程组
        // boss只是处理连接请求，真正和客户端业务处理，会交给worker完成
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置线程队列等待连接个数
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)//开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))//配置固定长度接收缓存区分配器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // web基于http协议的解码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            // 对大数据流的支持
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            //对http message进行聚合，聚合成FullHttpRequest或FullHttpResponse
                            ch.pipeline().addLast(new HttpObjectAggregator(1024 * 64));
                            ch.pipeline().addLast(httpRequestHandler);
                            // 添加我们自定义的channel处理器
                            ch.pipeline().addLast(webSocketServerHandler);
                        }
                    });
            // 绑定一个端口并且同步，生成一个channelfuture
            serverChannelFuture = bootstrap.bind(port).sync();
            // 对关闭通道进行监听
            serverChannelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            e.printStackTrace();
        } finally {
            // 关闭主从线程池
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

    /**
     * 描述：关闭Netty Websocket服务器，主要是释放连接
     *     连接包括：服务器连接serverChannel，
     *     客户端TCP处理连接bossGroup，
     *     客户端I/O操作连接workerGroup
     *
     *     若只使用
     *         bossGroupFuture = bossGroup.shutdownGracefully();
     *         workerGroupFuture = workerGroup.shutdownGracefully();
     *     会造成内存泄漏。
     */
    public void close() {
        serverChannelFuture.channel().close();
        Future<?> bossGroupFuture = boss.shutdownGracefully();
        Future<?> workerGroupFuture = worker.shutdownGracefully();

        try {
            bossGroupFuture.await();
            workerGroupFuture.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort(int port) {
        return this.port;
    }

    @Override
    public void run() {
        start();
    }
}
