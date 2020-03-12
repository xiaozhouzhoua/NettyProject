package com.demo.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@SuppressWarnings("all")
public class HttpServer {
    public static void main(String[] args) {
        // 创建两个线程组，boss组只负责处理连接请求，work组进行业务处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 使用链式编程来进行设置
        try {
            bootstrap.group(bossGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务端的通道实现
                    .childHandler(new HttpServerInitializer());
            System.out.println("服务器已经准备好了");

            // 绑定一个端口并且同步启动服务器，生成一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();

            // 给ChannelFuture注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("绑定端口8888成功！");
                    } else {
                        System.out.println("绑定端口8888失败！");
                    }
                }
            });
            // 对关闭通道进行监听，有关闭事件时进行关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
