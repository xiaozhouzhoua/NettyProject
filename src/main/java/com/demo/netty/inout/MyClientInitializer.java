package com.demo.netty.inout;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@SuppressWarnings("all")
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 出站编码器 客户端消息->服务端
        pipeline.addLast(new MyLongToByteEncoder());
        // 入站解码器 服务端消息->客户端
        pipeline.addLast(new MyByteToLongDecoder());
        // 自定义处理器
        pipeline.addLast(new MyClientHandler());
    }
}
