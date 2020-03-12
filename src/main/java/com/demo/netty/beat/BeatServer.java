package com.demo.netty.beat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class BeatServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 使用链式编程来进行设置
        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))// 在bossGroup中增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * 加入一个netty提供的IdleStateHandler处理空闲状态的处理器
                             * readerIdleTime：表示多长时间没有读了，会发送一个心跳检测包，检测是否连接
                             * writerIdleTime：表示多长时间没有写了，会发送一个心跳检测包，检测是否连接
                             * readerIdleTime：表示多长时间没有读写了，会发送一个心跳检测包，检测是否连接
                             * 心跳检测包：IdleStateEvent，通过心跳检测才能真正地感知到服务是否正常
                             */
                            pipeline.addLast(new IdleStateHandler(3,
                                    5,7, TimeUnit.SECONDS));
                            /**
                             * 加入对空闲检测进一步处理的自定义处理器，
                             * 因为IdleStateEvent触发后，会传递给管道的下一个handler，
                             * 触发其userEventTriggerd，在该方法中去处理空闲情况
                             */
                            pipeline.addLast("beatServerHandler", new BeatServerHandler());
                        }
                    });
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
