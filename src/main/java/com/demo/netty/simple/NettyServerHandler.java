package com.demo.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler需要继承netty规定好的某个适配器，才能称之为handler
 */
@SuppressWarnings("all")
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件，这里可以读取客户端发送的消息
     * ctx含有管道pipeline、地址和通道channel
     * msg就是客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器端ctx：" + ctx);
        // 使用netty提供的Bytebuf，而不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("客户端发送的消息是："
                + buf.toString(CharsetUtil.UTF_8));

        System.out.println("客户端地址："
                + ctx.channel().remoteAddress());
    }

    /**
     * 读取数据完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓冲并刷新，write + flush，还需要对发送的数据进行编码
        ctx.writeAndFlush(
                Unpooled.copiedBuffer("hello, 客户端",
                        CharsetUtil.UTF_8)
        );
    }

    /**
     * 处理异常，一般需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
