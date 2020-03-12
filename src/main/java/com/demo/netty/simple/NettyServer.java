package com.demo.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@SuppressWarnings("all")
public class NettyServer {
    public static void main(String[] args) {
        // 创建两个线程组，boss组只负责处理连接请求，work组进行业务处理
        // 可以指定子线程个数，不指定时按CPU核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 使用链式编程来进行设置
        try {
            bootstrap.group(bossGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel作为服务端的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .handler(null) // handler对应bossGroup，childHandler对应workGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 给workerGroup的EventLoop对应的管道设置处理器
                        // 给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 不同客户端对应不同的SocketChannel
                            System.out.println("客户端SocketChannel的hashcode："
                                    + socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NettyServerHandler2());
                        }
                    });
            System.out.println("服务器已经准备好了");

            // 绑定一个端口并且同步启动服务器，生成一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            // 给ChannelFuture注册监听器，监控我们关心的事件
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("绑定端口6668成功！");
                    } else {
                        System.out.println("绑定端口6668失败！");
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
