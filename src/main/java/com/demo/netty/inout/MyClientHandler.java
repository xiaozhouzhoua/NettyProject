package com.demo.netty.inout;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@SuppressWarnings("all")
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                Long msg) throws Exception {
        // 接收服务端消息
        System.out.println("服务端的IP：" + ctx.channel().remoteAddress());
        System.out.println("收到服务端消息：" + msg);
    }

    /**
     * 重写channelActive，发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);

        // 测试16个字节非long类型数据
        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
