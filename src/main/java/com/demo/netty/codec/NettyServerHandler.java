package com.demo.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler需要继承netty规定好的某个适配器，才能称之为handler
 */
@SuppressWarnings("all")
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    /**
     * 读取数据事件，这里可以读取客户端发送的消息
     * ctx含有管道pipeline、地址和通道channel
     * msg就是客户端发送的数据
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 读取从客户端发送的不同对象
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("客户端发送的数据：id-" + student.getId()
                    + "，name-" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("客户端发送的数据：age-" + worker.getAge()
                    + "，name-" + worker.getName());
        } else {
            System.out.println("传输的类型不正确！");
        }

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
