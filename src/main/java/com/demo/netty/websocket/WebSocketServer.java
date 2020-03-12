package com.demo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

@SuppressWarnings("all")
public class WebSocketServer {
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
                            // 因为是基于http协议的，使用http的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 因为http数据在传输过程中是分段的，这里将其聚合起来，避免多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // webSocket是以帧(frame)的形式传递，浏览器请求形式：ws://localhost:8888/xxx
                            // WebSocketServerProtocolHandler的核心功能就是将HTTP协议升级为ws协议，即保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/xxx"));
                            // 自定义handler，处理业务逻辑，包含帧的处理，WebSocketFrame的相关子类
                            pipeline.addLast(new WebSocketServerHandler());
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
