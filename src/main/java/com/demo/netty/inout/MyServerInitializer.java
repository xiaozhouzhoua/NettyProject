package com.demo.netty.inout;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 入站解码器 - 客户端消息->服务端
        pipeline.addLast(new MyByteToLongDecoder());
        // 出站编码器 - 服务端消息->客户端
        pipeline.addLast(new MyLongToByteEncoder());
        // 自定义处理器
        pipeline.addLast(new MyServerHandler());
    }
}
