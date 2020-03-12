package com.demo.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个Handler需要继承netty规定好的某个适配器，才能称之为handler
 */
@SuppressWarnings("all")
public class NettyServerHandler2 extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件，这里可以读取客户端发送的消息
     * ctx含有管道pipeline、地址和通道channel
     * msg就是客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 模拟非常耗时的业务-> 异步执行 -> 提交该channel到对应的NIOEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                ctx.writeAndFlush(Unpooled.copiedBuffer("耗时任务一完成",
                        CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常：" + e.getMessage());
            }
        });

        // 可以加多个任务到taskQueue
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
                ctx.writeAndFlush(Unpooled.copiedBuffer("耗时任务二完成",
                        CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常：" + e.getMessage());
            }
        });

        // 用户自定义定时任务，该任务提交到scheduledTaskQueue中
        ctx.channel().eventLoop().schedule(() -> {
            try {
                TimeUnit.SECONDS.sleep(20);
                ctx.writeAndFlush(Unpooled.copiedBuffer("定时任务完成",
                        CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                System.out.println("发生异常：" + e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("服务器端继续执行");
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
