package com.demo.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * 客户端自定义handler
 */
@SuppressWarnings("all")
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当通道就绪就会触发该方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送一个对象到服务器
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        if (0 == random) {
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(
                    MyDataInfo.MyMessage.DataType.StudentType
            ).setStudent(MyDataInfo.Student.newBuilder()
                    .setId(7).setName("007").build()).build();
        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(
                    MyDataInfo.MyMessage.DataType.WorkerType
            ).setWorker(MyDataInfo.Worker.newBuilder()
                    .setAge(22).setName("工作").build()).build();
        }
        ctx.writeAndFlush(myMessage);
    }

    /**
     * 当通道有读取事件时，会触发，这里的msg是服务器返回的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息："
                + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器端的地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
