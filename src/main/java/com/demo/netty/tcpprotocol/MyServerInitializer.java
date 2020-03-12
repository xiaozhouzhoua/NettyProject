package com.demo.netty.tcpprotocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 解码器接收消息
        pipeline.addLast(new MyMessageDecoder());
        // 编码器回送消息
        pipeline.addLast(new MyMessageEncoder());
        // 自定义处理器
        pipeline.addLast(new MyServerHandler());
    }
}
