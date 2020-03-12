package com.demo.netty.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    /**
     * 父类MessageToByteEncoder的acceptOutboundMessage方法会判断
     * 当前的msg是否是应该处理的类型，如果是就进行编码，否则就跳过编码
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg,
                          ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder encode 被调用！");
        System.out.println("msg=" + msg);
        out.writeLong(msg);
    }
}
