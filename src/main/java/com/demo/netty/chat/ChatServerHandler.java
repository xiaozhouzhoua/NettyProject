package com.demo.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@SuppressWarnings("all")
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个Channel组，管理所有的channel，GlobalEventExecutor是一个全局事件执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(
            GlobalEventExecutor.INSTANCE
    );
    /**
     * handlerAdded是第一个调用的方法，
     * 一旦连接建立，这个方法就会执行
     * 这里将当前channel加入到channelGroup
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户聊天信息推送给其他在线的客户
        channelGroup.writeAndFlush("[客户端]："
                + channel.remoteAddress() + ", 加入聊天.");
        channelGroup.add(channel);
    }

    /**
     * 表示channel处于活动状态，提示客户端上线了
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线");
    }

    /**
     * 表示channel处于非活动状态，提示客户端下线了
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "下线");
    }

    /**
     * 表示channel断开连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户聊天信息推送给其他在线的客户
        channelGroup.writeAndFlush("[客户端]："
                + channel.remoteAddress() + ", 离开聊天.");
        System.out.println("客户端数量：" + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前channel
        Channel channel = ctx.channel();

        // 遍历channelGroup，去除自己的消息信息
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户端]："
                        + channel.remoteAddress() + "发送消息：" + msg);
            } else {
                ch.writeAndFlush("[自己]：" + msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
