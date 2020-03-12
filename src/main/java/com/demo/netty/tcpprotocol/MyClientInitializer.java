package com.demo.netty.tcpprotocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@SuppressWarnings("all")
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入编码器发送消息
        pipeline.addLast(new MyMessageEncoder());
        // 加入解码器接收消息
        pipeline.addLast(new MyMessageDecoder());
        // 自定义处理器
        pipeline.addLast(new MyClientHandler());
    }
}
