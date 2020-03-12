package com.demo.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

@SuppressWarnings("all")
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个netty提供的httpServerCode，即http编解码器[coder-decoder]
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        // 加入自定义处理器
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
    }
}
